<template>
  <div class="app-container">
    <el-form ref="form" :model="homework" label-width="80px" :rules="rules">
      <el-form-item label="标题" prop="title">
        <el-input v-model="homework.title" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" :rows="10" v-model="homework.description" placeholder="请输入描述" />
      </el-form-item>
      <el-form-item label="截止时间" prop="deadline">
        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" v-model="homework.deadline" type="datetime" placeholder="选择日期时间"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-row type="flex" class="row-bg" justify="end">
          <el-button type="primary" @click="onSubmit('form')" :disabled="disabled">提交</el-button>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import * as homeworkApi from "./api";
import { mapGetters } from "vuex";
export default {
  watch: {
    homework: function () {
      console.log("homework改变了");
    },
  },
  computed: {
    ...mapGetters(["classId"]),
  },
  props: {
    update: {
      type: Boolean,
      default: false,
    },
    homework: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      disabled: false,
      rules: {
        title: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { max: 20, message: "长度不能超过20个字符", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa50-9a-zA-Z]+$/,
            message: "请不要包含特殊字符",
            trigger: "blur",
          },
        ],
        description: [{ required: true, message: "不能为空", trigger: "blur" }],
        deadline: [{ required: true, message: "不能为空", trigger: "blur" }],
        state: [{ required: true, message: "请选择状态", trigger: "blur" }],
      },
    };
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.update) {
            this.updateHomework();
          } else {
            this.insertHomework();
          }
        } else {
          return false;
        }
      });
    },
    insertHomework() {
      this.disabled = true;
      this.homework.classId = this.classId;
      homeworkApi
        .insertHomework(this.homework)
        .then((result) => {
          if (result.success) {
            this.$message.success("添加作业成功");
            this.$refs["form"].resetFields();
          } else {
            this.$message.error(result.message);
          }
          this.disabled = false;
        })
        .catch((error) => {
          this.disabled = false;
        });
    },
    updateHomework() {
      this.disabled = true;
      this.homework.classId = this.classId;
      homeworkApi
        .updateHomework(this.homework)
        .then((result) => {
          if (result.success) {
            this.$message.success("修改作业成功");

          } else {
            this.$message.error(result.message);
          }
          this.disabled = false;
        })
        .catch((error) => {
          this.disabled = false;
        });
    },
  },
};
</script>
