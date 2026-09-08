#!/bin/bash
# ============================================================================
#  计算器 APP - 一键本地构建 APK 脚本
#  用法: ./build_apk.sh
#  前置: 安装 JDK 17 (https://adoptium.net) 并设置 JAVA_HOME
# ============================================================================
set -e

echo "=========================================="
echo "  计算器 APP - 构建 APK"
echo "=========================================="

# 1. 检查 Java
if ! command -v java >/dev/null 2>&1; then
    echo "[错误] 未找到 Java，请先安装 JDK 17 并设置 JAVA_HOME"
    echo "       下载: https://adoptium.net/temurin/releases/?version=17"
    exit 1
fi
echo "[1/4] Java 版本:"
java -version 2>&1 | head -1

# 2. 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# 3. 确保 gradlew 有执行权限
chmod +x gradlew 2>/dev/null || true

# 4. 构建（优先用 gradlew，否则尝试系统 gradle）
if [ -x "./gradlew" ] && [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "[2/4] 使用 Gradle Wrapper 构建..."
    ./gradlew assembleDebug --no-daemon
else
    echo "[2/4] 未找到 gradle-wrapper.jar，尝试使用系统 gradle..."
    if command -v gradle >/dev/null 2>&1; then
        gradle assembleDebug --no-daemon
    else
        echo "[!] 未找到 gradle，尝试自动下载 Gradle 8.0.2..."
        GRADLE_ZIP="gradle-8.0.2-bin.zip"
        GRADLE_DIR="$SCRIPT_DIR/.gradle-download"
        mkdir -p "$GRADLE_DIR"
        if [ ! -f "$GRADLE_DIR/$GRADLE_ZIP" ]; then
            curl -L -o "$GRADLE_DIR/$GRADLE_ZIP" \
                "https://services.gradle.org/distributions/gradle-8.0.2-bin.zip"
        fi
        if [ ! -d "$GRADLE_DIR/gradle-8.0.2" ]; then
            unzip -q "$GRADLE_DIR/$GRADLE_ZIP" -d "$GRADLE_DIR/"
        fi
        GRADLE_BIN="$GRADLE_DIR/gradle-8.0.2/bin/gradle"
        chmod +x "$GRADLE_BIN"
        "$GRADLE_BIN" assembleDebug --no-daemon
    fi
fi

# 5. 输出结果
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
    echo ""
    echo "=========================================="
    echo "  ✅ 构建成功！"
    echo "  APK 位置: $(pwd)/$APK_PATH"
    echo "=========================================="
    ls -lh "$APK_PATH"
else
    echo "[错误] 未找到生成的 APK，请检查上方构建日志"
    exit 1
fi
