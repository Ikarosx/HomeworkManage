<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input placeholder="学校" label="username" maxlength="10" size="mini" style="width: 200px"/>
      <el-select v-model="queryParams.systemType" placeholder="系统类型" size="mini">
        <el-option key="3" label="系统类型" value="-1"/>
        <el-option key="0" label="正方" value="0"/>
        <el-option key="1" label="其他" value="1"/>
      </el-select>
      <el-select v-model="queryParams.state" placeholder="状态" size="mini">
        <el-option key="3" label="状态" value="-1"/>
        <el-option key="0" label="正常" value="0"/>
        <el-option key="1" label="禁用" value="1"/>
      </el-select>
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
      <el-table-column label="商品" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.goodName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.st }}</span>
        </template>
      </el-table-column>
      <el-table-column label="卖家" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学校" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.schoolName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="举报次数/浏览次数" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.reportCount +'/' + scope.row.browseCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.state == 1 ? 'success' : 'danger' "
          >{{ scope.row.state == 1 ? '正常' : '下架'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="操作" min-width="100">
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
          >编辑</el-button>
          <el-button
            class="hidden-sm-and-down"
            size="mini"
            type="danger"
            @click="clickDelete(scope.row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="修改用户" :visible.sync="editDialog">
      <!-- <edit-school :update="true" :school="editSchool" /> -->
    </el-dialog>
  </div>
</template>
<script>
import { states, filters } from "@/../config/system";
import * as shopApi from "./api";
import "element-ui/lib/theme-chalk/display.css";
export default {
  components: {
    // "edit-school": EditSchool
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
      editDialog: false,
      page: 1,
      size: 10,
      queryParams: {},
      editGood: {}
    };
  },
  methods: {
    loadData() {
      this.listLoading = true;
      shopApi
        .listGoods(this.page, this.size, this.queryParams)
        .then(response => {
          if (response.success) {
            this.list = response.queryResult.list;
            this.total = response.queryResult.total;
          } else {
            this.$message.error(response.message);
          }
          this.listLoading = false;
        })
        .catch(error => {
          this.$message.error(error);
          this.listLoading = false;
        });
    },
    handleEdit(index, row) {
      this.editSchool = row;
      console.log(row);
      this.editDialog = true;
    },
    clickDelete(index) {
      shopApi
        .deleteGood(index)
        .then(res => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.error(res.message);
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    }
  }
};
</script>
