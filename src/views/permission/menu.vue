<template>
  <div class="app-container">
    <div class="filter-container"></div>
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
      <el-table-column label="权限" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.menuName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="系统类型" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.systemType | schoolSystemFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.state | schoolStateFilter">{{
            scope.row.state == 0 ? "正常" : "禁用"
          }}</el-tag>
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
    <el-dialog title="修改用户" :visible.sync="editDialog">
      <edit-school :update="true" :school="editSchool" />
    </el-dialog>
  </div>
</template>
<script>
export default {};
</script>
