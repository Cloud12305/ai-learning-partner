module.exports = {
    root: true,
    env: {
        node: true,
        'vue/setup-compiler-macros': true
    },
    extends: [
        'plugin:vue/vue3-essential',
        'eslint:recommended'
    ],
    parserOptions: {
        ecmaVersion: 2020
    },
    rules: {
        'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-undef': 'off', // 关闭未定义检查
        'no-unused-vars': 'off', // 关闭未使用变量检查
        'no-useless-catch': 'off', // 关闭无用catch检查
        'vue/no-unused-vars': 'off', // 关闭Vue未使用变量检查
        'vue/multi-word-component-names': 'off' // 关闭多单词组件名检查
    },
    globals: {
        // 定义Vue 3的编译宏为全局变量
        defineProps: 'readonly',
        defineEmits: 'readonly',
        defineExpose: 'readonly',
        withDefaults: 'readonly'
    }
}