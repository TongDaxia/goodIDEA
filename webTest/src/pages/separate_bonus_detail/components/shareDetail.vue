<template>
    <!-- 分红面板 -->
    <div class="share-div">
        <div class="title-box">
            <span class="title">分红</span>
        </div>
        <div v-if="isEmpty">
            <p class="no-data">暂无相关信息</p>
        </div>
        <div class="content" v-if="tableData.length">
            <div class="table-wrapper flex-row">
                <div>
                    <table class="first" ref="tableLeft">
                        <tr>
                            <th>权益登记日</th>
                        </tr>
                        <tr v-for="d in tableData">
                            <td>{{d.rightsRegisterDate}}</td>
                        </tr>
                    </table>
                </div>
                <div class="auto" ref="autoTab">
                    <div class="auto-box">
                        <table class="second" ref="tableRight">
                            <tr>
                                <th>最新单位分红</th>
                                <th>除息日</th>
                                <th>红利发放日</th>
                            </tr>
                            <tr v-for="d in tableData">
                                <td>{{d.perBonus}}</td>
                                <td>{{d.exDividendDate}}</td>
                                <td>{{d.bonusReleaseDate}}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
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
                    const req ={...param, skip: 0, pagesize: this.curPageSize, type: '1'};
                    this.$get('/finchinaAPP/nonStandard/getShareOutBonusDetail.action', req).then(res => {
                        this.totalCount = res.data.totalCount;
                        this.curPage = 1;
                        this.totalPage = Math.ceil(this.totalCount / this.curPageSize);
                        this.tableData = res.data.list;
                        //左右表格高度统一
                        this.initTableLayout();
                        resolve();
                    }).catch(err => {
                        this.isEmpty = true;
                        resolve();
                    });
                })
            },
            //左右表格高度统一
            initTableLayout () {
                this.$nextTick(()=>{
                    /*
                    两边高度不一致
                    const leftTr = this.$refs.tableLeft.getElementsByTagName('tr'),//表格左一列
                        rightTr = this.$refs.tableRight.getElementsByTagName('tr');//表格右三列
                    for (let j = 0,l = leftTr.length; j < l; j++) {
                        rightTr[j].style.height = leftTr[j].clientHeight +'px';
                    }*/
                    this.$refs.autoTab.style.height = this.$refs.tableRight.clientHeight - 7 +'px';
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
                const req ={...param, skip: this.curPageSize * (this.curPage - 1), pagesize: this.curPageSize, type: '1'};
                this.$get('/finchinaAPP/nonStandard/getShareOutBonusDetail.action', req).then(res => {
                    this.tableData = res.data.list;
                    this.initTableLayout();
                })
            },
        },
        components: { noFound },
    }
</script>

<style lang="less">
    @import "../../../assets/less/base";

    #app {
        .share-div{
            background-color: white;
            margin-bottom: 0.16rem;
            .content{
                table{
                    table-layout: fixed;
                    width: 100%;
                    text-align: right;
                    &.first{
                        width: 2.4rem;
                        th,td{text-align: left;}
                    }
                    &.second{
                        th,td{
                            text-align: right;
                            width: 2.2rem;
                        }
                    }
                }
                .table-wrapper{
                    padding: 0 0.32rem;
                    position: relative;
                    &:before,&:after{
                        content: '';
                        top:0.1rem;
                        position: absolute;
                        display: inline-block;
                        border: 0.1rem solid transparent;
                    }
                    &:before{
                        border-right-color: #8c8c8c;
                        left: 2.35rem;
                    }
                    &:after{
                        border-left-color: #8c8c8c;
                        right:0;
                    }
                    .auto{
                        overflow: hidden;
                        .auto-box{
                            overflow-x: scroll;
                            position: relative;
                            -webkit-overflow-scrolling: touch;
                        }
                    }
                }
            }
        }
    }
</style>