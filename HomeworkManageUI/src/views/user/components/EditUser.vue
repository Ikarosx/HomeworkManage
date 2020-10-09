<template>
  <div class="app-container">
    <el-form ref="form" :model="user" label-width="80px" :rules="rules">
      <el-form-item label="学号" prop="studentId">
        <el-input v-model="user.studentId" :disabled="update" />
      </el-form-item>
      <el-form-item label="姓名" prop="username">
        <el-input v-model="user.username" :disabled="update" />
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="user.nickName" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="user.password" type="password" />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="user.sex">
          <el-radio :label="0">男</el-radio>
          <el-radio :label="1">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="学校" prop="schoolId">
        <el-select v-model="user.schoolId" placeholder="请选择所属学校">
          <el-option
            v-for="school in schools"
            :key="school.id"
            :label="school.schoolName"
            :value="school.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="身份" prop="selectedRole">
        <el-checkbox-group v-model="selectedRole">
          <el-checkbox :label="1">管理员</el-checkbox>
          <el-checkbox :label="2">学生</el-checkbox>
          <el-checkbox :label="3">老师</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-radio-group v-model="user.state">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">未激活</el-radio>
          <el-radio :label="2">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-row type="flex" class="row-bg" justify="end">
          <el-button
            type="primary"
            @click="onSubmit('form')"
            :disabled="disabled"
            >提交
          </el-button>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>
<style>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
<script>
import * as schoolApi from "@/views/school/api";
import * as userApi from "../api";
export default {
  created() {
    this.selectedRole = [];
    if (this.update) {
      for (let index in this.user.bdRoleList) {
        this.selectedRole.push(parseInt(this.user.bdRoleList[index].id));
      }
    }
    this.loadSchool();
  },
  watch: {
    "user.bdRoleList": function() {
      if (this.update) {
        this.selectedRole = [];
        for (let index in this.user.bdRoleList) {
          this.selectedRole.push(parseInt(this.user.bdRoleList[index].id));
        }
      }
    }
  },
  filters: {
    identityFilter(status) {
      const statusMap = {
        0: "success",
        1: "info",
        2: "danger"
      };
      return statusMap[status];
    }
  },
  props: {
    update: {
      type: Boolean,
      default: false
    },
    user: {
      type: Object,
      default: () => {
        return {
          bdRoleList: []
        };
      }
    }
  },
  data() {
    var selectedRoleValidate = (rule, value, callback) => {
      if (this.selectedRole.length == 0) {
        return callback(new Error("角色必须选取"));
      }
      callback();
    };
    return {
      selectedRole: [],
      disabled: false,
      schools: [],
      rules: {
        studentId: [
          { required: true, message: "请填写学号", trigger: "blur" },
          { min: 1, max: 20, message: "长度1-20", trigger: "blur" },
          {
            pattern: /^[0-9a-zA-Z]+$/,
            message: "只能填写数字或字母",
            trigger: "blur"
          }
        ],
        password: [
          { required: true, message: "请填写密码", trigger: "blur" },
          {
            pattern: /^[\w]{6,20}$/,
            message: "长度为6-20的数字、字母和下划线",
            trigger: "blur"
          }
        ],
        username: [
          { required: true, message: "请填写姓名", trigger: "blur" },
          { min: 1, max: 10, message: "长度1-10", trigger: "blur" },
          {
            pattern: /^[a-zA-Z\u4e00-\u9fa5]+$/,
            message: "只能填写字母和汉字",
            trigger: "blur"
          }
        ],
        nickName: [
          { required: true, message: "请填写昵称", trigger: "blur" },
          { min: 1, max: 10, message: "长度1-10", trigger: "blur" },
          {
            pattern: /^[0-9a-zA-Z\u4e00-\u9fa5]+$/,
            message: "只能填写数字、字母和汉字",
            trigger: "blur"
          }
        ],
        sex: [{ required: true, message: "性别必须选取", trigger: "blur" }],
        schoolId: [
          { required: true, message: "学校必须选取", trigger: "blur" }
        ],
        selectedRole: [
          { validator: selectedRoleValidate, trigger: "blur", required: true }
        ],
        state: [{ required: true, message: "状态必须选取", trigger: "blur" }]
      }
    };
  },
  methods: {
    onSubmit(formName) {
      console.log(this.user);
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.update) {
            this.updateUser();
          } else {
            this.insertUser();
          }
        } else {
          return false;
        }
      });
    },
    insertUser() {
      this.disabled = true;
      this.user.bdRoleList = [];
      for (let index in this.selectedRole) {
        this.user.bdRoleList.push({ id: this.selectedRole[index] });
      }
      userApi
        .insertUser(this.user)
        .then(res => {
          if (res.success) {
            this.$message.success(res.message);
            this.$refs["form"].resetFields();
            this.selectedRole = [];
          } else {
            this.$message.error(res.message);
          }
          this.disabled = false;
        })
        .catch(err => {
          this.$message.error(err);
          this.disabled = false;
        });
    },
    updateUser() {
      this.disabled = true;
      this.user.bdRoleList = [];
      for (let index in this.selectedRole) {
        this.user.bdRoleList.push({ id: this.selectedRole[index] });
      }
      userApi
        .updateUser(this.user, this.user.id)
        .then(res => {
          if (res.success) {
            this.$message.success(res.message);
            this.$emit("loadData");
          } else {
            this.$message.error(res.message);
          }
          this.disabled = false;
        })
        .catch(err => {
          this.$message({
            message: err,
            type: "error"
          });
          this.disabled = false;
        });
    },
    loadSchool() {
      schoolApi
        .listSchools()
        .then(res => {
          if (res.success) {
            this.schools = res.queryResult.list;
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
