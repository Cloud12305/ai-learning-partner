<!-- AcademicImportModal.vue -->
<template>
  <a-modal
      v-model:visible="visible"
      title="智能导入成绩"
      width="800px"
      :footer="null"
  >
    <div class="space-y-4">
      <!-- 模板下载 -->
      <div class="bg-blue-50 p-4 rounded-lg">
        <h4 class="font-semibold mb-2">导入说明</h4>
        <p class="text-sm text-gray-600 mb-3">请下载模板文件，按照格式填写数据后上传</p>
        <Button type="primary" @click="downloadTemplate" class="mr-2">
          <i class="fa fa-download mr-1"></i>下载模板
        </Button>
        <Button @click="showExample = true">
          <i class="fa fa-eye mr-1"></i>查看示例
        </Button>
      </div>

      <!-- 文件上传 -->
      <div class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
        <input
            type="file"
            ref="fileInput"
            @change="handleFileUpload"
            accept=".xlsx,.xls,.csv"
            class="hidden"
        />
        <div class="text-4xl text-gray-400 mb-2">
          <i class="fa fa-cloud-upload"></i>
        </div>
        <p class="text-gray-600 mb-2">点击或拖拽文件到此处上传</p>
        <p class="text-sm text-gray-500">支持 Excel、CSV 格式</p>
        <Button type="dashed" @click="$refs.fileInput.click()" class="mt-3">
          选择文件
        </Button>
      </div>

      <!-- 数据预览 -->
      <div v-if="previewData.length > 0" class="mt-4">
        <h4 class="font-semibold mb-2">数据预览</h4>
        <a-table
            :dataSource="previewData"
            :columns="previewColumns"
            size="small"
            :pagination="{ pageSize: 5 }"
        />
        <div class="flex justify-end mt-4 space-x-2">
          <Button @click="clearPreview">取消</Button>
          <Button type="primary" @click="handleImport" :loading="importing">
            确认导入
          </Button>
        </div>
      </div>
    </div>
  </a-modal>

  <!-- 示例弹窗 -->
  <a-modal
      v-model:visible="showExample"
      title="数据格式示例"
      width="600px"
      :footer="null"
  >
    <a-table
        :dataSource="exampleData"
        :columns="previewColumns"
        size="small"
    />
  </a-modal>
</template>

<script>
import { ref } from 'vue'
import { Button, Table, Modal, message } from 'ant-design-vue'
import * as XLSX from 'xlsx'

export default {
  components: {
    Button,
    'a-table': Table,
    'a-modal': Modal
  },
  props: {
    visible: Boolean
  },
  emits: ['close', 'import'],
  setup(props, { emit }) {
    const fileInput = ref(null)
    const previewData = ref([])
    const showExample = ref(false)
    const importing = ref(false)

    const previewColumns = [
      { title: '课程ID', dataIndex: 'courseId', key: 'courseId' },
      { title: '学年', dataIndex: 'academicYear', key: 'academicYear' },
      { title: '学期', dataIndex: 'semester', key: 'semester' },
      { title: '成绩', dataIndex: 'score', key: 'score' }
    ]

    const exampleData = [
      { courseId: 'CS101', academicYear: '2023-2024', semester: 1, score: 92 },
      { courseId: 'MA101', academicYear: '2023-2024', semester: 1, score: 88 },
      { courseId: 'GE101', academicYear: '2023-2024', semester: 1, score: 85 }
    ]

    const handleFileUpload = (event) => {
      const file = event.target.files[0]
      if (!file) return

      const reader = new FileReader()
      reader.onload = (e) => {
        try {
          const data = new Uint8Array(e.target.result)
          const workbook = XLSX.read(data, { type: 'array' })
          const firstSheet = workbook.Sheets[workbook.SheetNames[0]]
          const jsonData = XLSX.utils.sheet_to_json(firstSheet)

          // 数据验证和转换
          const validatedData = jsonData.map(item => ({
            courseId: item.courseId || item.课程ID,
            academicYear: item.academicYear || item.学年,
            semester: parseInt(item.semester || item.学期),
            score: parseFloat(item.score || item.成绩)
          })).filter(item => item.courseId && item.academicYear && item.semester && !isNaN(item.score))

          previewData.value = validatedData
          message.success(`成功解析 ${validatedData.length} 条记录`)
        } catch (error) {
          message.error('文件解析失败，请检查文件格式')
        }
      }
      reader.readAsArrayBuffer(file)
    }

    const handleImport = async () => {
      if (previewData.value.length === 0) {
        message.warning('没有可导入的数据')
        return
      }

      importing.value = true
      try {
        emit('import', previewData.value)
      } finally {
        importing.value = false
      }
    }

    const clearPreview = () => {
      previewData.value = []
      if (fileInput.value) {
        fileInput.value.value = ''
      }
    }

    const downloadTemplate = () => {
      const ws = XLSX.utils.json_to_sheet(exampleData)
      const wb = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(wb, ws, '成绩模板')
      XLSX.writeFile(wb, '成绩导入模板.xlsx')
    }

    return {
      fileInput,
      previewData,
      showExample,
      importing,
      previewColumns,
      exampleData,
      handleFileUpload,
      handleImport,
      clearPreview,
      downloadTemplate
    }
  }
}
</script>