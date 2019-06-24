<template>
    <!-- 拆分面板 -->
    <div class="split-div">
        <div class="title-box">
            <span class="title">拆分</span>
        </div>
        <div v-if="isEmpty">
            <p class="no-data">暂无相关信息</p>
        </div>
        <div class="content" v-if="tableData.length">
            <table class="split-pane">
                <tr>
                    <th>除息日</th>
                    <th>拆分比例(每份)</th>
                </tr>
                <tr v-for="d in tableData">
                    <td>{{d.exDividendDate}}</td>
                    <td>{{d.splitProportion}}</td>
                </tr>
            </table>
            <div class="pagination" v-if="totalCount > curPageSize">
                <p>
                    <span class="first" :class="{'disabled':curPage === 1}" @click="changePage($event)">首页</span>
                    <span class="prev" :class="{'disabled':curPage === 1}" @click="changePage($event)">上一页</span>
                    <span class="num">{{curPage}}/{{totalPage}}</span>
                    <span class="next" :class="{'disabled':curPage === totalPage}" @click="changePage($event)">下一页</span>
                    <span class="last" :class="{'disabled':curPage === totalPage}" @click="changePage($event)">末页</span>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../../components/noFound'
    const param = common.getParams();

    export default {
        data() {
            return {
                tableData:[],
                curPageSize: 10,//当前页面每页展示条数
                totalCount: 0,//数据总数量
                curPage: 1,//当前页码
                totalPage: 1,//总页数
                isEmpty: false,
            }
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();
        },
        methods: {
            // 初次加载，页面初始化
            loadData () {
                return new Promise(resolve => {
                    const req ={...param, skip: 0, pagesize: this.curPageSize, type: "2"};
                    this.$get('/finchinaAPP/nonStandard/getShareOutBonusDetail.action', req).then(res => {
                        this.totalCount = res.data.totalCount;
                        this.curPage = 1;
                        this.totalPage = Math.ceil(this.totalCount / this.curPageSize);
                        this.tableData = res.data.list;
                        resolve();
                    }).catch(err => {
                        this.isEmpty = true;
                        resolve();
                    });
                })
            },
            //分页按钮点击
            changePage(e){
                const c = e.target.className || '';
                if(c.includes('disabled')) return false;
                if(c.includes('first')) this.curPage = 1;
                if(c.includes('last')) this.curPage = this.totalPage;
                if(c.includes('prev')) this.curPage -= 1;
                if(c.includes('next')) this.curPage += 1;
                this.loadMoreData();
            },
            //信息分页
            loadMoreData(){
                const req ={...param, skip: this.curPageSize * (this.curPage - 1), pagesize: this.curPageSize, type:
                        "2"};
                this.$get('/finchinaAPP/nonStandard/getShareOutBonusDetail.action', req).then(res => {
                    this.tableData = res.data.list;
                })
            },
        },
        components: { noFound },
    }
</script>

<style lang="less">
    @import "../../../assets/less/base";

    #app {
        .split-div{
            background-color: white;
            margin-bottom: 0.48rem;
            .content{
                .split-pane{
                    padding: 0.24rem 0.32rem 0;
                    table-layout: fixed;
                    width: 100%;
                    text-align: right;
                    th,td{
                        &:first-child{
                            text-align: left;
                        }
                    }
                }
            }
        }
    }
</style>