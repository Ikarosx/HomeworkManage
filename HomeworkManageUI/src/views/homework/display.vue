<template>
  <el-container>
    <!-- <div class="filter-container"> -->
    <el-main>
      <el-row>
        <el-radio v-model="queryParams.status" label="1">全部</el-radio>
        <el-radio v-model="queryParams.status" label="2">已完成</el-radio>
        <el-radio v-model="queryParams.status" label="3">未完成</el-radio>
      </el-row>
      <!-- <el-button size="small" type="primary" @click="loadData">
        <span>筛选</span>
      </el-button>-->
      <!-- </div> -->
      <el-row>
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
          <el-table-column label="名称" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column label="描述" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.description }}</span>
            </template>
          </el-table-column>
          <el-table-column label="截止日期" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.deadline }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.finish === true ? 'success':new Date(scope.row.deadline) < new Date()?'warning' : 'danger'"
              >
                {{
                scope.row.finish ? "已完成" : new Date(scope.row.deadline) < new Date() ? "已超时" : "未完成"
                }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="created_at" label="操作" min-width="80">
            <template slot-scope="scope">
              <el-button
                type="success"
                circle
                size="mini"
                icon="el-icon-finished"
                :disabled="new Date(scope.row.deadline) < new Date() || scope.row.finish === true"
                @click="handleSubmit(scope.$index, scope.row)"
              />
              <el-button
                type="info"
                circle
                size="mini"
                icon="el-icon-s-data"
                @click="handleCollect(scope.$index, scope.row)"
              />
              <el-button
                type="primary"
                circle
                size="mini"
                icon="el-icon-edit"
                v-show="isAdmin || isClassAdmin"
                @click="handleEdit(scope.$index, scope.row)"
              />
              <el-button
                type="danger"
                circle
                size="mini"
                icon="el-icon-delete"
                v-show="isAdmin || isClassAdmin"
                @click="clickDelete(scope.row.id)"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-dialog title="修改作业" :visible.sync="editDialog">
        <edit-homework :update="true" :homework="editHomework" />
      </el-dialog>
      <el-dialog title="提交作业" :visible.sync="submitDialog">
        <submit-homework :homeworkId="submitHomeworkId" @submitHomeworkOk="submitHomeworkOk" />
      </el-dialog>
      <el-dialog title="查看作业提交情况" :visible.sync="homeworkInfoDialog">
        <homework-info :homeworkFinishInfos="homeworkFinishInfos" :homeworkId="homeworkId"/>
      </el-dialog>
    </el-main>
  </el-container>
</template>
<script>
import { states, filters } from "@/../config/system";
import { mapGetters } from "vuex";
import * as homeworkApi from "./api";
import EditHomework from "./add";
import SubmitHomework from "./components/SubmitHomework";
import HomeworkInfo from "./components/HomeworkInfo";
export default {
  components: {
    "edit-homework": EditHomework,
    SubmitHomework,
    HomeworkInfo,
  },
  computed: {
    ...mapGetters(["isClassAdmin", "isAdmin","classId"]),
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
      editHomework: {},
      submitDialog: false,
      editDialog: false,
      homeworkInfoDialog: false,
      submitHomeworkId: "",
      homeworkFinishInfos:{},
      homeworkId: ""
    };
  },
  methods: {
    handleCollect(index, row) {
      let params = {
        homeworkId: row.id,
        classId: this.classId,
      };
      this.homeworkId = row.id;
      homeworkApi
        .getHomeworkFinishInfo(params)
        .then((result) => {
          if (result.success) {
            this.homeworkFinishInfos = result.data.list;
            this.homeworkInfoDialog = true;
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {
        });
    },
    submitHomeworkOk() {
      // 提交成功之后关闭窗口
      this.submitDialog = false;
      // 重新加载数据
      this.loadData();
    },
    loadData() {
      this.listLoading = true;
      homeworkApi
        .listHomeworks()
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
      this.submitHomeworkId = row.id;
      this.submitDialog = true;
    },
    handleEdit(index, row) {
      this.editHomework = row;
      this.editDialog = true;
    },
    clickDelete(index) {
      homeworkApi
        .deleteHomework(index)
        .then((result) => {
          if (result.success) {
            this.$message.success('删除成功');
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
