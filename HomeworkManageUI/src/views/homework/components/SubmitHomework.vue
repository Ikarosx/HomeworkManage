<template>
  <div class="app-container">
    <el-row>
      <el-upload
        class="upload-demo"
        :action="uploadUrl"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :before-remove="beforeRemove"
        :on-success="onSuccess"
        multiple
        :limit="10"
        :on-exceed="handleExceed"
        :http-request="uploadFile"
      >
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip"></div>
      </el-upload>
    </el-row>
    <el-row type="flex" justify="end">
      <el-button type="primary" @click="insertManageHomeworkUser">提交作业</el-button>
    </el-row>
  </div>
</template>
<script>
import { systemConfig } from "@/../config/system";
import axios from "axios";
import * as homeworkApi from "../api";
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["classId"]),
  },
  props: {
    homeworkId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      disabled: false,
      rules: {
        content: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      homework: {},
      fileList: [],
      files:[],
      uploadUrl: systemConfig.apiUrl + "/file/upload",
    };
  },
  methods: {
    uploadFile(params) {
      console.log(params);
      let formData = new FormData();
      formData.append("file", params.file);
      let config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      };
      axios
        .post(this.uploadUrl, formData, config)
        .then((result) => {
          if (result.data.success) {
            params.onSuccess(result.data, params.file, this.fileList);
            this.$message.success(result.data.message);
          } else {
            console.log("not success");
            this.$message.error(result.data.message);
            params.onError(error);
          }
        })
        .catch((error) => {
          console.log(error);
          params.onError(error);
        });
    },
    onSuccess(response, file, fileList) {
      this.files = fileList;
    },
    handleRemove(file, fileList) {
      this.files = fileList;
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 10 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + fileList.length
        } 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    insertManageHomeworkUser() {
      if (this.files.length == 0) {
        this.$message.error("上传文件不得为0");
        return;
      }
      
      this.disabled = true;

      var fileIds = "";
      for(let index in this.files){
        fileIds += this.files[index].response.data.id + ",";
      };
      if (fileIds.length > 0) {
        fileIds = fileIds.substring(0, fileIds.length - 1);
      }
      var params = {
        homeworkId: this.homeworkId,
        fileIds: fileIds
      };
      homeworkApi
        .insertManageHomeworkUser(params)
        .then((result) => {
          if (result.success) {
            this.$message.success("提交作业成功");
            this.$emit("submitHomeworkOk");
          } else {
            this.$message.error(result.message);
          }
          this.disabled = false;
        })
        .catch((error) => {
          this.disabled = false;
        });
    },
  },
};
</script>
