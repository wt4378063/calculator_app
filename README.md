# 计算器 APP (Android)

一个功能完整的安卓计算器应用，采用 Kotlin 开发，Material Design 风格深色主题。

## ✨ 功能

- ➕ 四则运算（加、减、乘、除）
- 🔢 小数运算
- 📊 百分比计算（`50 × 20% = 10`）
- 🔄 正负号切换（`+/-`）
- ⌫ 逐位删除（DEL）
- 🧹 一键清除（C）
- 🎯 连续计算（出结果后可直接继续运算）
- 🛡️ 除零错误处理

## 🧪 测试结果

核心计算引擎 8 项单元测试全部通过：
```
✅ 2+3 = 5        ✅ 6×7 = 42       ✅ 10-4 = 6       ✅ 15÷3 = 5
✅ 50×20% = 10    ✅ 2+3×4 = 14     ✅ 1÷0 = 错误      ✅ 0.1+0.2 = 0.3
```

---

## 🚀 方式一：用 GitHub Actions 自动出 APK（**推荐，最简单**）

1. 在 GitHub 上新建一个仓库（如 `calculator-app`）
2. 把本项目**整个文件夹** push 上去：
   ```bash
   git init
   git add .
   git commit -m "init calculator app"
   git remote add origin https://github.com/<你的用户名>/calculator-app.git
   git push -u origin main
   ```
3. 进入仓库 → **Actions** 标签页 → 点击 **"Build APK"** workflow → **Run workflow**
4. 等待约 3-5 分钟，构建完成后在 **Artifacts** 处下载 `calculator-debug-apk`（内含 `app-debug.apk`）

> 后续每次 `git push` 也会自动触发构建，APK 会自动更新。

---

## 🛠️ 方式二：本地构建 APK

### 前置环境
- **JDK 17**（[下载 Temurin 17](https://adoptium.net/temurin/releases/?version=17)）
- 设置 `JAVA_HOME` 指向 JDK 目录
- （可选）Android SDK，或通过 Android Studio 打开自动配置

### 步骤
```bash
# 1. 克隆/进入项目
cd calculator_app

# 2. 一键构建（脚本会自动下载 Gradle 并编译）
./build_apk.sh
```

或用 **Android Studio**（最简单，无需手动配环境）：
1. 打开 Android Studio → **Open** → 选择本项目文件夹
2. 等待 Gradle 自动同步、下载依赖
3. 点击 ▶️ **Run** 直接运行到模拟器/真机
4. 或菜单 **Build → Build Bundle(s) / APK(s) → Build APK(s)** 生成 APK

生成的 APK 路径：`app/build/outputs/apk/debug/app-debug.apk`

---

## 📂 项目结构

```
calculator_app/
├── .github/workflows/build-apk.yml   # GitHub Actions 自动构建
├── build_apk.sh                      # 本地一键构建脚本
├── build.gradle                      # 根构建配置
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat
└── app/
    ├── build.gradle                  # 模块配置
    ├── proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/calculator/
        │   │   ├── MainActivity.kt         # UI + 交互逻辑
        │   │   └── CalculatorEngine.kt     # 计算引擎（递归下降解析器）
        │   └── res/
        │       ├── layout/activity_main.xml
        │       ├── values/{colors,strings,styles,themes}.xml
        │       └── drawable/button_bg.xml
        └── test/
            └── java/com/example/calculator/
                └── CalculatorEngineTest.kt  # 单元测试
```

---

## 🔑 技术要点

- **递归下降解析器**：`CalculatorEngine` 正确处理运算符优先级（`×÷%` 优先于 `+-`），可扩展支持括号
- **浮点精度处理**：`0.1 + 0.2 = 0.3`，不会输出 `0.30000000000000004`
- **关注点分离**：计算引擎独立于 UI，可单独测试、便于扩展科学计算功能

## 📱 运行要求
- Android 5.0（API 21）及以上

## 📄 许可
MIT License
