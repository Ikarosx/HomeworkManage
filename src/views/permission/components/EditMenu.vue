<template>
  <div class="app-container">
    <el-form ref="form" :model="school" label-width="80px" :rules="rules">
      <el-form-item label="学校" prop="schoolName">
        <el-input v-model="school.schoolName" />
      </el-form-item>
      <el-form-item label="官网系统" prop="systemType">
        <el-select
          v-model="school.systemType"
          placeholder="系统类型"
          size="mini"
        >
          <el-option
            v-for="item in systemTypes"
            :key="item.id"
            :label="item.systemName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-radio-group v-model="school.state">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-row type="flex" class="row-bg" justify="end">
          <el-button
            type="primary"
            @click="onSubmit('form')"
            :disabled="disabled"
            >提交</el-button
          >
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import * as schoolApi from "../api";
export default {
  created() {
    this.loadSystemTypes();
  },
  watch: {
    school: function() {
      console.log("school改变了");
    }
  },
  props: {
    update: {
      type: Boolean,
      default: false
    },
    school: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {
    return {
      systemTypes: [],
      disabled: false,
      rules: {
        schoolName: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { max: 20, message: "长度不能超过20个字符", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa50-9a-zA-Z]+$/,
            message: "请不要包含特殊字符",
            trigger: "blur"
          }
        ],
        systemType: [
          { required: true, message: "请选择官网系统类型", trigger: "blur" }
        ],
        state: [{ required: true, message: "请选择状态", trigger: "blur" }]
      }
    };
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.update) {
            this.updateSchool();
          } else {
            this.insertSchool();
          }
        } else {
          return false;
        }
      });
    },
    loadSystemTypes() {
      var _this = this;
      schoolApi
        .listSchoolSystems()
        .then(res => {
          if (res.success) {
            _this.systemTypes = res.queryResult.list;
          } else {
            this.$message.error("查询系统类型失败");
          }
        })
        .catch(err => {
          this.$message.error("查询系统类型失败");
        });
    },
    insertSchool() {
      var _this = this;
      this.disabled = true;
      schoolApi
        .insertSchool(this.school)
        .then(res => {
          if (res.success) {
            this.$message({
              message: "添加学校成功",
              type: "success"
            });
            this.$refs["form"].resetFields();
          } else {
            this.$message.error(res.message);
          }
          _this.disabled = false;
        })
        .catch(err => {
          this.$message.error(err);
          _this.disabled = false;
        });
    },
    updateSchool() {
      var _this = this;
      this.disabled = true;
      schoolApi
        .updateSchool(this.school)
        .then(res => {
          if (res.success) {
            this.$message({
              message: "修改学校成功",
              type: "success"
            });
          } else {
            this.$message.error(res.message);
          }
          _this.disabled = false;
        })
        .catch(err => {
          this.$message.error(err);
          _this.disabled = false;
        });
    }
  }
};
</script>
