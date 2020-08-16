<template>
  <div class="app-container">
    <el-form ref="form" :model="good" label-width="80px" :rules="rules">
      <el-form-item label="名称" prop="goodName">
        <el-input v-model="good.goodName" />
      </el-form-item>
      <el-form-item label="图片" prop="goodName">
        <el-button type="primary" @click="toggleShow">上传图片</el-button>
        <el-carousel height="270px" width="480px" v-show="img.list != null && img.list.length !=0">
          <el-carousel-item v-for="item in 4" :key="item">
            <h3 class="small">{{ item }}</h3>
          </el-carousel-item>
        </el-carousel>
      </el-form-item>
      <image-upload
        field="img"
        @crop-success="cropSuccess"
        @crop-upload-success="cropUploadSuccess"
        @crop-upload-fail="cropUploadFail"
        v-model="show"
        :width="480"
        :height="270"
        url="/upload"
        :params="params"
        :headers="headers"
        img-format="png"
        :langExt="img.langExt"
        :noCircle="true"
        :noSquare="false"
      ></image-upload>

      <el-form-item label="价格" prop="price">
        <el-input v-model="good.price" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryActive">
        <el-cascader
          :options="category.categoryList"
          v-model="category.categoryActive"
          :props="category.props"
          :show-all-levels="false"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="描述">
        <el-input type="textarea" v-model="good.description"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-radio-group v-model="good.state">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">禁用</el-radio>
        </el-radio-group>
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
import * as goodApi from "../api";
import ImageUpload from "vue-image-crop-upload";

export default {
  components: {
    "image-upload": ImageUpload
  },
  created() {},
  watch: {
    good: function() {
      console.log("good改变了");
    }
  },
  props: {
    update: {
      type: Boolean,
      default: false
    },
    good: {
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
      category: {
        categoryList: [],
        categoryActive: [],
        props: {}
      },
      params: {},
      img: {
        langExt: {
          preview: "图片预览"
        },
        list: []
      },
      headers: {
        smail: "*_~"
      },
      show: false,
      rules: {
        goodName: [
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
    /**
     * crop success
     *
     * [param] imgDataUrl
     * [param] field
     */
    cropSuccess(imgDataUrl, field) {
      console.log("-------- crop success --------");
      this.imgDataUrl = imgDataUrl;
    },
    /**
     * upload success
     *
     * [param] jsonData   服务器返回数据，已进行json转码
     * [param] field
     */
    cropUploadSuccess(jsonData, field) {
      console.log("-------- upload success --------");
      console.log(jsonData);
      console.log("field: " + field);
    },
    /**
     * upload fail
     *
     * [param] status    server api return error status, like 500
     * [param] field
     */
    cropUploadFail(status, field) {
      console.log("-------- upload fail --------");
      console.log(status);
      console.log("field: " + field);
    },
    toggleShow() {
      this.show = !this.show;
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.update) {
            this.updategood();
          } else {
            this.insertgood();
          }
        } else {
          return false;
        }
      });
    },
    insertgood() {
      this.disabled = true;
      goodApi
        .insertgood(this.good)
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
          this.disabled = false;
        })
        .catch(err => {
          this.$message.error(err);
          this.disabled = false;
        });
    },
    updategood() {
      this.disabled = true;
      goodApi
        .updategood(this.good)
        .then(res => {
          if (res.success) {
            this.$message({
              message: "修改学校成功",
              type: "success"
            });
          } else {
            this.$message.error(res.message);
          }
          this.disabled = false;
        })
        .catch(err => {
          this.$message.error(err);
          this.disabled = false;
        });
    }
  }
};
</script>
