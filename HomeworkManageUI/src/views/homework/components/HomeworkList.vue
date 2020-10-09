<template>
  <div class="app-container">
    <el-row>
      <el-checkbox
        :indeterminate="isIndeterminate"
        v-model="checkAll"
        @change="handleCheckAllChange"
        >全选</el-checkbox
      >
      <div style="margin: 15px 0"></div>
      <el-checkbox-group
        v-model="checkFileList"
        @change="handleCheckedFilesChange"
      >
        <el-checkbox v-for="file in fileList" :label="file.id" :key="file.id">{{
          file.fileName
        }}</el-checkbox>
      </el-checkbox-group>
    </el-row>
    <el-row type="flex" justify="end">
      <el-button type="primary" @click="download">下载文件</el-button>
    </el-row>
  </div>
</template>
<script>
import * as homeworkApi from "../api";
import { systemConfig } from "@/../config/system";
import { mapGetters } from "vuex";
export default {
  computed: {},
  watch: {
    homeworkUserId() {
      this.getHomeworkListByHomeworkUserId(this.homeworkUserId);
    },
  },
  mounted() {
    this.getHomeworkListByHomeworkUserId(this.homeworkUserId);
  },
  props: {
    homeworkUserId: {
      type: String,
      default: "-1",
    },
  },
  data() {
    return {
      homework: {},
      checkAll: false,
      checkFileList: [],
      fileList: [],
      isIndeterminate: true,
    };
  },

  methods: {
    handleCheckAllChange(val) {
      let ids = [];
      this.fileList.forEach((file) => {
        ids.push(file.id);
      });
      this.checkFileList = val ? ids : [];
      this.isIndeterminate = false;
    },
    handleCheckedFilesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.fileList.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.fileList.length;
    },
    getHomeworkListByHomeworkUserId(homeworkUserId) {
      homeworkApi
        .getHomeworkListByHomeworkUserId(homeworkUserId)
        .then((result) => {
          if (result.success) {
            this.fileList = result.data.fileList;
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    download() {
      if (this.checkFileList == null || this.checkFileList.length == 0) {
        this.$message.error("下载文件列表不能为空");
        return;
      }
      let homeworkIds = this.checkFileList.join(",");
      let url =
        systemConfig.apiUrl +
        "/manageHomeworkUser/" +
        this.homeworkUserId +
        "/download?homeworkIds=" +
        homeworkIds;
      window.open(url);
      // this.openPostWindow(url, homeworkIds);
    },
    openPostWindow(url, homeworkIds) {
      var newWin = window.open(),
        formStr = "";
      //设置样式为隐藏，打开新标签再跳转页面前，如果有可现实的表单选项，用户会看到表单内容数据
      formStr =
        '<form style="visibility:hidden;" method="POST" action="' +
        url +
        '">' +
        '<input type="hidden" name="homeworkIds" value="' +
        homeworkIds +
        '" />' +
        "</form>";

      newWin.document.body.innerHTML = formStr;
      newWin.document.forms[0].submit();

      return newWin;
    },
  },
};
</script>
