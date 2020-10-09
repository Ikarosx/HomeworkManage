<template>
  <div class="app-container">
    <el-row type="flex" justify="end">
      <el-button type="primary" @click="clickAdd">添加</el-button>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">{{ scope.$index }}</template>
      </el-table-column>
      <el-table-column label="系统名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.systemName }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="created_at"
        label="操作"
        min-width="80"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            circle
            size="mini"
            class="hidden-sm-and-up"
            icon="el-icon-edit"
            @click="handleEdit(scope.$index, scope.row)"
          />
          <el-button
            type="danger"
            circle
            size="mini"
            class="hidden-sm-and-up"
            icon="el-icon-delete"
            @click="clickDelete(scope.row.id)"
          />
          <el-button
            class="hidden-sm-and-down"
            size="mini"
            type="primary"
            @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button
          >
          <el-button
            class="hidden-sm-and-down"
            size="mini"
            type="danger"
            @click="clickDelete(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="editDialog.title" :visible.sync="showDialog">
      <edit-system
        :update="editDialog.update"
        :system="editSystem"
        @loadData="loadData"
      />
    </el-dialog>
  </div>
</template>
<script>
import { states, filters } from "@/../config/system";
import * as schoolApi from "./api";
import EditSystem from "./components/EditSystem";
export default {
  components: {
    "edit-system": EditSystem
  },
  filters: filters,
  created() {
    this.loadData();
  },
  data() {
    return {
      list: null,
      listLoading: false,
      states: states,
      editDialog: {
        update: true,
        title: ""
      },
      page: 1,
      size: 10,
      queryParams: {},
      editSystem: {},
      showDialog: false
    };
  },
  methods: {
    loadData() {
      this.listLoading = true;
      schoolApi
        .listSchoolSystems()
        .then(response => {
          this.list = response.queryResult.list;
          this.total = response.queryResult.total;
          this.listLoading = false;
        })
        .catch(error => {
          console.log(error);
          this.listLoading = false;
        });
    },
    handleEdit(index, row) {
      this.editSystem = row;
      this.editDialog.update = true;
      this.editDialog.title = "修改系统";
      this.showDialog = true;
    },
    clickAdd() {
      this.editDialog.update = false;
      this.editDialog.title = "添加系统";
      this.showDialog = true;
    },
    clickDelete(id) {
      schoolApi
        .deleteSchoolSystem(id)
        .then(res => {
          if (res.success) {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.loadData();
          } else {
            this.$message.error(res.message);
          }
        })
        .catch(err => {
          this.$message({
            message: err,
            type: "error"
          });
        });
    }
  }
};
</script>
