const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false, // 完全禁用 ESLint 检查
  devServer: {
    port: 3000,
    open: true
  }
})