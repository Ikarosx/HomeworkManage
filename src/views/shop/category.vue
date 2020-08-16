<template>
  <div class="app-container">
    <el-tree
      :data="list"
      show-checkbox
      ref="tree"
      node-key="id"
      highlight-current
      :props="props"
      :expand-on-click-node="false"
      :default-expanded-keys="defaultExpandedKeys"
      @node-expand="nodeExpand"
      @node-collapse="nodeCollapse"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button type="text" size="mini" @click="() => clickAdd(node, data)">增加</el-button>
          <el-button
            type="text"
            size="mini"
            v-if="data.id != 1"
            @click="() => clickDelete(node, data)"
          >删除</el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog title="增加分类" :visible.sync="editDialog">
      <edit-category
        :categoryActive="categoryActive"
        :fromParent="true"
        @loadData="loadData"
      />
    </el-dialog>
  </div>
</template>
<script>
import * as categoryApi from "./api";
import "element-ui/lib/theme-chalk/display.css";
import EditCategory from "./components/EditCategory";
export default {
  components: {
    "edit-category": EditCategory
  },
  created() {
    this.loadData();
  },
  data() {
    return {
      list: null,
      editDialog: false,
      tempData: null,
      categoryActive: [],
      defaultExpandedKeys:[],
      props: {
        label: "name",
        children: "children"
      }
    };
  },
  methods: {
    nodeExpand(data){
      this.defaultExpandedKeys.push(data.id);
    },
    nodeCollapse(data){
      this.defaultExpandedKeys = this.defaultExpandedKeys.filter(t => t!=data.id);
    },
    loadData() {
      this.listLoading = true;
      categoryApi
        .listCategoryStruct()
        .then(response => {
          if (response.success) {
            this.list = response.queryResult.list;
            this.total = response.queryResult.total;
          } else {
            this.$message.error(response.message);
          }
          this.listLoading = false;
        })
        .catch(error => {
          this.$message({
            message: error,
            type: "error"
          });
          this.listLoading = false;
        });
    },
    clickDelete(node, data) {
      categoryApi
        .deleteCategory(data.id)
        .then(res => {
          if (res.success) {
            this.$message.success(res.message);
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === data.id);
            children.splice(index, 1);
          } else {
            this.$message.error(res.message);
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    clickAdd(node, data) {
      this.categoryActive = [];
      this.pushCategoryActive(node);
      this.editDialog = true;
    },
    pushCategoryActive(node) {
      // 如果还存在父节点
      if (node.parent) {
        // 继续递归
        this.pushCategoryActive(node.parent);
        this.categoryActive.push(node.data.id);
      }
    }
  }
};
</script>
