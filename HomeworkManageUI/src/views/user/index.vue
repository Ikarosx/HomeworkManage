<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input placeholder="姓名" label="username" maxlength="10" size="mini" style="width: 200px" v-model="queryParams.username" />
      <el-input placeholder="学号" label="studentno" maxlength="10" size="mini" style="width: 200px" v-model="queryParams.studentId"/>
      <template>
        <el-select v-model="queryParams.sex" placeholder="性别" size="mini">
          <el-option
            v-for="item in sex"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <template>
        <el-select v-model="queryParams.roleId" placeholder="身份" size="mini">
          <el-option
            v-for="item in roles"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <template>
        <el-select v-model="queryParams.state" placeholder="状态" size="mini">
          <el-option
            v-for="item in states"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <el-button size="small" type="primary" @click="loadData">
        <span>筛选</span>
      </el-button>
    </div>
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
      <el-table-column label="学号" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.studentId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center">
        <template slot-scope="scope">{{ scope.row.username }}</template>
      </el-table-column>
      <el-table-column label="昵称" align="center">
        <template slot-scope="scope">{{ scope.row.nickName }}</template>
      </el-table-column>
      <el-table-column label="性别" align="center" width="60">
        <template slot-scope="scope">
          {{
          scope.row.sex == 0 ? "男" : "女"
          }}
        </template>
      </el-table-column>
      <el-table-column label="学校" align="center">
        <template slot-scope="scope">{{ scope.row.schoolName }}</template>
      </el-table-column>
      <el-table-column class-name="status-col" label="角色" align="center">
        <template slot-scope="scope">
          <el-tag
            v-for="item in scope.row.bdRoleList"
            :key="item.id"
            :type="item.id | roleFilter"
          >{{ item.roleName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" min-width="60" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.state | statusFilter">
            {{
            scope.row.state == 0
            ? "正常"
            : scope.row.state == 1
            ? "未激活"
            : "禁用"
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" min-width="80">
        <template slot-scope="scope">
          <el-button
            type="primary"
            circle
            size="mini"
            class="hidden-sm-and-up"
            icon="el-icon-edit"
            @click="handleEdit(scope.row.id, scope.row)"
          />
          <el-button
            type="danger"
            circle
            size="mini"
            class="hidden-sm-and-up"
            icon="el-icon-delete"
            @click="handleDelete(scope.row.id, scope.row)"
          />
          <el-button
            class="hidden-sm-and-down"
            size="mini"
            type="primary"
            @click="handleEdit(scope.row.id, scope.row)"
          >编辑</el-button>
          <el-button
            class="hidden-sm-and-down"
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.id, scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="修改用户" :visible.sync="editUserDialog">
      <edit-user
        :update="update"
        :user="editUser"
        @loadData="loadData"
        :editUserDialog="editUserDialog"
      />
    </el-dialog>
    <pagination v-show="true" :total="total" page.sync="3" limit.sync="10" @pagination="loadData" />
  </div>
</template>
<style>
.el-row {
  margin-bottom: 20px;
}
</style>
<script>
import * as userApi from "@/views/user/api";
import "element-ui/lib/theme-chalk/display.css";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import EditUser from "./components/EditUser";
import { states, roles, filters, sex } from "@/../config/system";
export default {
  components: { Pagination, "edit-user": EditUser },
  filters: filters,
  data() {
    return {
      list: null,
      update: false,
      listLoading: true,
      total: 100,
      page: 1,
      size: 10,
      queryParams: {},
      editUserDialog: false,
      editUser: {},
      states: states,
      roles: roles,
      sex: sex
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      this.listLoading = true;
      userApi
        .getUserList(this.page, this.size, this.queryParams)
        .then((response) => {
          if (response.success) {
            this.list = response.queryResult.list;
            this.total = response.queryResult.total;
          } else {
            this.$message.error(response.message);
          }
          this.listLoading = false;
        })
        .catch((err) => {
          this.$message.error(err.message);
          this.listLoading = false;
        });
    },
    handleEdit(index, row) {
      this.editUser = row;
      this.update = true;
      this.editUserDialog = true;
    },
    handleDelete(index, row) {
      userApi
        .deleteUser(index)
        .then((res) => {
          if (res.success) {
            this.$message({
              message: res.message,
              type: "success",
            });
            this.loadData();
          } else {
            this.$message.error(res.message);
          }
        })
        .catch((err) => {
          this.$message({
            message: err.message,
            type: "error",
          });
        });
    },
  },
};
</script>
