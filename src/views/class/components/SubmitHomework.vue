<template>
  <div class="app-container">
    <el-form ref="form" :model="homework" label-width="80px" :rules="rules">
      <el-form-item label="内容" prop="content">
        <el-input type="textarea" :rows="10" v-model="homework.content" />
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
import * as homeworkApi from "../api";
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["classId"]),
  },
  props: {
    homeworkId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      disabled: false,
      rules: {
        content: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      homework: {},
    };
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.insertManageHomeworkUser();
        } else {
          return false;
        }
      });
    },
    insertManageHomeworkUser() {
      this.disabled = true;
      this.homework.homeworkId = this.homeworkId;
      homeworkApi
        .insertManageHomeworkUser(this.homework)
        .then((result) => {
          if (result.success) {
            this.$message.success("提交作业成功");
            this.$emit("submitHomeworkOk");
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
