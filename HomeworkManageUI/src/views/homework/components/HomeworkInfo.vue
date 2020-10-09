<template>
  <div class="app-container">
    <el-row>
      <el-table
        :data="homeworkFinishInfos"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column align="center" label="ID" width="50px">
          <template slot-scope="scope">{{ scope.$index }}</template>
        </el-table-column>
        <el-table-column align="center" label="学号">
          <template slot-scope="scope">{{ scope.row.studentNo }}</template>
        </el-table-column>
        <el-table-column label="昵称" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.nickname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? "已完成" : "未完成" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="附件" align="center">
          <template slot-scope="scope">
            <el-button
              icon="el-icon-search"
              circle
              type="info"
              size="mini"
              v-if="scope.row.status === 1"
              @click="showHomeworkFileList(scope.row.homeworkUserId)"
            ></el-button>
            <el-button
              icon="el-icon-download"
              circle
              type="success"
              size="mini"
              v-if="scope.row.status === 1"
            ></el-button>
          </template>
        </el-table-column>
        <el-table-column label="完成日期" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-dialog
      title="作业列表"
      :visible.sync="homeworkListDialog"
      append-to-body
    >
      <homework-list :homeworkUserId="homeworkUserId" />
    </el-dialog>
    <el-row type="flex" justify="end">
      <el-button type="primary" @click="exportExcel">导出Excel数据</el-button>
    </el-row>
  </div>
</template>
<script>
import * as homeworkApi from "../api";
import HomeworkList from "./HomeworkList";
import { mapGetters } from "vuex";
export default {
  components: {
    HomeworkList,
  },
  computed: {
    ...mapGetters(["classId"]),
  },
  props: {
    homeworkFinishInfos: {
      type: Array,
      default: [],
    },
    homeworkId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      homework: {},
      homeworkListDialog: false,
      homeworkUserId: "",
    };
  },
  methods: {
    showHomeworkFileList(homeworkUserId) {
      this.homeworkUserId = homeworkUserId;
      this.homeworkListDialog = true;
    },

    exportExcel() {
      let params = {
        classId: this.classId,
        id: this.homeworkId,
      };
      homeworkApi.downloadHomeworkFinishInfos(params);
    },
  },
};
</script>
