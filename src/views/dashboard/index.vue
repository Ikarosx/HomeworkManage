<template>
  <div class="dashboard-container">
    <el-container v-if="user.classId === null || user.classId === ''">
      <el-main>
        <el-row type="flex" justify="end">
          <el-input v-model="manageClass.name" placeholder="请输入要创建的班级名称" style="width:200px"></el-input>&nbsp;
          <el-button type="primary" @click="insertManageClass">创建班级</el-button>
        </el-row>
        <el-row>
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="id" label="ID"></el-table-column>
            <el-table-column prop="name" label="班级名称"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" @click="insertManageClassUser(scope.$index, scope.row)">加入</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
      </el-main>
    </el-container>
    <el-container v-else>
      <el-main>
        <el-card class="box-card" shadow="hover">
          <div slot="header" class="clearfix">
            <span>{{manageClassDetailInfo.name}}</span>
            <el-button
              v-if="manageClassDetailInfo.adminUser.id !== user.id"
              style="float: right;"
              type="danger"
              size="mini"
              @click="deleteManageClassUser"
            >退出班级</el-button>
            <el-button
              v-else
              style="float: right;"
              type="danger"
              size="mini"
              @click="deleteClass"
            >解散班级</el-button>
          </div>
          <el-row>
            <div>班级人数：{{manageClassDetailInfo.num}}人</div>
          </el-row>
          <el-row>
            <div>班级管理员：{{manageClassDetailInfo.adminUser.nickname}}</div>
          </el-row>
          <el-row>
            <el-table :data="manageClassDetailInfo.members" stripe style="width: 100%">
              <el-table-column prop="id" label="ID"></el-table-column>
              <el-table-column prop="studentNo" label="学号"></el-table-column>
              <el-table-column prop="nickname" label="姓名"></el-table-column>
              <el-table-column prop="createTime" label="加入时间"></el-table-column>
            </el-table>
          </el-row>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>
<style>
.el-row {
  margin-bottom: 20px;
}
</style>
<script>
import { mapGetters } from "vuex";
import * as manageClassApi from "./api";

export default {
  name: "Dashboard",
  data() {
    return {
      tableData: [],
      manageClass: {},
      user: { classId: "", id: "" },
      manageClassDetailInfo: {
        adminUser: {
          id: "",
          nickname: "",
        },
        name: "班级名称",
      },
    };
  },
  created() {
    this.user = JSON.parse(window.localStorage.getItem("user"));
    if (this.user.classId !== null && this.user.classId !== "") {
      this.getManageClassDetailInfo(this.user.classId);
    } else {
      // 判断是否加入了班级
      manageClassApi
        .getUserById(this.user.id)
        .then((result) => {
          if (result.success) {
            // 如果已经更新了班级
            if (result.data.user.classId != null) {
              this.user = result.data.user;
              window.localStorage.setItem("user", JSON.stringify(result.data.user));
              location.reload();
            }
          } else {
            this.$message.error(result.message);
          }
          this.listAllManageClass();
          this.$alert("你目前没有加入任何班级，请创建/加入一个班级", "提示", {
            confirmButtonText: "确定",
            callback: (action) => {},
          });
        })
        .catch((error) => {
          this.$message.error(error.message);
        });
    }
  },
  mounted() {},
  methods: {
    deleteClass() {
      manageClassApi
        .deleteClass(this.user.classId)
        .then((result) => {
          if (result.success) {
            this.user.classId = "";
            window.localStorage.setItem("user", JSON.stringify(this.user));
            this.$message.success("解散班级成功，好聚好散，等待两秒刷新");
            setTimeout(() => {
              location.reload();
            }, 2000);

            // this.listAllManageClass();
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    insertManageClassUser(index, row) {
      const joinClassParam = {
        userId: this.user.id,
        classId: row.id,
      };
      manageClassApi
        .insertManageClassUser(joinClassParam)
        .then((result) => {
          if (result.success) {
            this.$message.success("已经申请加入班级");
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    deleteManageClassUser() {
      manageClassApi
        .deleteManageClassUser(this.user.id)
        .then((result) => {
          if (result.success) {
            this.$message.success("退出班级成功，请等待两秒刷新");
            setTimeout(() => {
              this.user.classId = "";
              window.localStorage.setItem("user", JSON.stringify(this.user));
              location.reload();
            }, 2000);
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    getManageClassDetailInfo(classId) {
      manageClassApi
        .getManageClassDetailInfo(classId)
        .then((result) => {
          if (result.success) {
            this.manageClassDetailInfo = result.data.manageClassDetailInfo;
            // if (this.manageClassDetailInfo.adminUser.id === this.user.id) {
            //   this.user.roles.push("classAdmin");
            //   window.localStorage.setItem("user", JSON.stringify(this.user));
            // }
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    listAllManageClass() {
      manageClassApi
        .listAllManageClass()
        .then((result) => {
          if (result.success) {
            this.tableData = result.data.list;
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
    insertManageClass() {
      manageClassApi
        .insertManageClass(this.manageClass)
        .then((result) => {
          if (result.success) {
            this.$message.success("创建班级成功，请等待两秒刷新");
            // 刷新路由
            setTimeout(() => {
              this.user.classId = result.data.classId;
              window.localStorage.setItem("user", JSON.stringify(this.user));
              location.reload();
            }, 2000);
            // this.getManageClassDetailInfo(this.user.classId);
          } else {
            this.$message.error(result.message);
          }
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
