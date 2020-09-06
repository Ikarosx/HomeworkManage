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
            <el-tag :type="scope.row.status === 1 ? 'success': 'danger'">
              {{
              scope.row.status === 1 ? "已完成" : "未完成"
              }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="完成日期" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row type="flex" justify="end">
       <el-button type="primary" @click="exportExcel">导出Excel数据</el-button> 
    </el-row>
  </div>
</template>
<script>
import * as homeworkApi from "../api";
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["classId"]),
  },
  props: {
    homeworkFinishInfos: {
      type: Array,
      default: [],
    },
    homeworkId:{
      type: String,
      default:""
    }
  },
  data() {
    return {
      homework: {},
    };
  },
  methods: {
    exportExcel(){
      let params = {
        classId: this.classId,
        id: this.homeworkId
      }
      homeworkApi.downloadHomeworkFinishInfos(params);
    },
  },
};
</script>
