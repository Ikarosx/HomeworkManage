<template>
  <div class="app-container">
    <el-form ref="form" :model="category" label-width="80px" :rules="rules">
      <el-form-item label="分类" prop="name">
        <el-input v-model="category.name" />
      </el-form-item>
      <el-form-item label="父类" prop="categoryActive">
        <el-cascader
          :options="categoryList"
          v-model="categoryActive"
          :props="props"
          :show-all-levels="false"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="category.show" active-text="显示" inactive-text="隐藏"></el-switch>
      </el-form-item>
      <el-form-item label="叶子结点">
        <el-switch v-model="category.leaf" active-text="是" inactive-text="否"></el-switch>
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
import * as categoryApi from "../api";
import qs from "qs";
export default {
  created() {
    this.loadCategoryStruct();
  },
  props: {
    fromParent: {
      type: Boolean,
      default: false
    },
    categoryActive: {
      type: Array,
      default: () => {
        return [];
      }
    }
  },
  data() {
    var categoryValidate = (rule, value, callback) => {
      if (this.categoryActive.length == 0) {
        return callback(new Error("不能为空"));
      }
      callback();
    };
    return {
      disabled: false,
      categoryList: [],
      props: {
        expandTrigger: "hover",
        value: "id",
        label: "name",
        children: "children",
        checkStrictly: true
      },
      category: {
        show: true,
        leaf: false
      },
      rules: {
        name: [
          { required: true, message: "请输入名称", trigger: "blur" },
          { max: 20, message: "长度不能超过20个字符", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa50-9a-zA-Z]+$/,
            message: "请不要包含特殊字符",
            trigger: "blur"
          }
        ],
        categoryActive: [
          { validator: categoryValidate, trigger: "blur", required: true }
        ]
      }
    };
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.update) {
            this.updateCategory();
          } else {
            this.insertCategory();
          }
        } else {
          return false;
        }
      });
    },
    loadCategoryStruct() {
      categoryApi
        .listCategoryStruct()
        .then(res => {
          if (res.success) {
            this.categoryList = this.handleLeaf(res.queryResult.list);
          } else {
            this.$message.error("查询分类结构失败");
          }
        })
        .catch(err => {
          this.$message.error("查询分类结构失败");
        });
    },
    insertCategory() {
      this.disabled = true;
      this.category.parentId = this.categoryActive[
        this.categoryActive.length - 1
      ];
      categoryApi
        .insertCategory(this.category)
        .then(res => {
          if (res.success) {
            this.$message.success(res.message);
            // 如果是从父组件过来的
            if (this.fromParent) {
              this.$emit("loadData");
            }
            this.$refs["form"].resetFields();
            this.loadCategoryStruct();
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
    updatecCategory() {
      this.disabled = true;
      categoryApi
        .updateCategory(this.category)
        .then(res => {
          if (res.success) {
            this.$message.success(res.message);
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
    handleLeaf(json) {
      for (let i = 0; i < json.length; i++) {
        if (!json[i].children || json[i].children.length < 1) {
          json[i].children = undefined;
        } else {
          this.handleLeaf(json[i].children);
        }
      }
      return json;
    }
  }
};
</script>
