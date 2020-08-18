<template>
  <el-container>
    <!-- <div class="filter-container"> -->
    <el-main>
      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="Loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column align="center" label="ID">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="学号" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.studentNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="昵称" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.nickname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请时间" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="created_at" label="操作" min-width="80">
          <template slot-scope="scope">
            <el-button
              type="success"
              circle
              size="mini"
              icon="el-icon-check"
              @click="handleSubmit(scope.$index, scope.row)"
            />
            <el-button
              type="danger"
              circle
              size="mini"
              icon="el-icon-close"
              @click="clickDelete(scope.$index, scope.row)"
            />
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>
<script>
import { states, filters } from "@/../config/system";
import { mapGetters } from "vuex";
import * as classApi from "./api";
export default {
  computed: {
    ...mapGetters(["isClassAdmin", "isAdmin", "classId"]),
  },
  filters: filters,
  created() {
    this.loadData();
  },
  watch: {
    "queryParams.status"(newValue, olaValue) {
      if (newValue == 1) {
        this.list = this.allList;
      } else if (newValue == 2) {
        this.list = this.allList.filter((item) => item.finish == true);
      } else {
        this.list = this.allList.filter((item) => item.finish == false);
      }
    },
  },
  data() {
    return {
      list: null,
      listLoading: false,
      allList: [],
      states: states,

      page: 1,
      size: 10,
      queryParams: { status: "1" },
      editClass: {},
      submitDialog: false,
      editDialog: false,
      submitClassId: "",
    };
  },
  methods: {
    submitClassOk() {
      // 提交成功之后关闭窗口
      this.submitDialog = false;
      // 重新加载数据
      this.loadData();
    },
    loadData() {
      this.listLoading = true;
      classApi
        .listWaitingUser(this.classId)
        .then((response) => {
          if (response.success) {
            this.list = response.data.list;
            this.allList = this.list;
            this.total = response.data.total;
          } else {
            this.$message.error(response.message);
          }
          this.listLoading = false;
        })
        .catch((error) => {
          this.listLoading = false;
        });
    },
    handleSubmit(index, row) {
      let params = {
        userId: row.id,
        classId: this.classId,
        status: 0,
      };
      classApi
        .updateClassUser(params)
        .then((result) => {
          if (result.success) {
            this.$message.success("同意成功");
            this.loadData();
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    clickDelete(index, row) {
      let params = {
        userId: row.id,
        classId: this.classId,
        status: 2,
      };
      classApi
        .updateClassUser(params)
        .then((result) => {
          if (result.success) {
            this.$message.success("拒绝申请成功");
            this.loadData();
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
  },
};
</script>
