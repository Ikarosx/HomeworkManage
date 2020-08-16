<template>
    <div class="app-container">
        <el-form ref="form" :model="system" label-width="80px" :rules="rules">
            <el-form-item label="系统" prop="systemName">
                <el-input v-model="system.systemName" />
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
import * as schoolApi from "../api"
import querystring from "querystring"
import axios from "axios"
import request from "@/utils/request"
export default {
    props:{
        update:{
            type: Boolean,
            default: false
        },
        system:{
            type: Object,
            default:{

            }
        }
    },
    data(){
        return {
            disabled: false,
            rules: {
                systemName: [
                    { required: true, message: '请输入名称', trigger: 'blur' },
                    { max: 10, message: '长度不能超过10个字符', trigger: 'blur' },
                    { pattern: /^[\u4e00-\u9fa50-9a-zA-Z]+$/ , message: "请不要包含特殊字符", trigger: 'blur'}
                ]
            }
        }
    },
    methods:{
        onSubmit(formName){
            var _this = this;
            this.$refs[formName].validate(valid => {
                if(valid){
                    if(_this.update){
                        _this.updateData();
                    }else{
                        _this.insertData();
                    }
                }else{
                    return false;
                }
            })
        },
        updateData(){
            this.disabled = true;
            schoolApi.updateSchoolSystem(this.system,this.system.id).then(res => {
                if(res.success){
                    this.$message({
                        message: '修改站点类型成功',
                        type: 'success'
                    });
                    this.$emit("loadData");
                }else{
                    this.$message.error(res.message);
                }
                this.disabled = false;
            }).catch(err => {
                this.$message({
                    message: err,
                    type: 'error'
                });
                this.disabled = false;
            })
        },
        insertData(){
            this.disabled = true;
            schoolApi.insertSchoolSystem(this.system).then(res => {
                if(res.success){
                    this.$message({
                        message: '添加站点类型成功',
                        type: 'success'
                    });
                    this.$emit("loadData");
                }else{
                    this.$message.error(res.message);
                }
                this.disabled = false;
            }).catch(err => {
                this.$message({
                    message: err,
                    type: 'error'
                });
                this.disabled = false;
            })
        }
    }
}
</script>