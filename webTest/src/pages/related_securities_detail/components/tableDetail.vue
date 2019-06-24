<template>
    <div class="content" :id="`mainContent${type}`">
        <no-found v-if="isEmpty"></no-found>
        <div class="main" v-if="tableData.length">
            <table class="scroll-tbody">
                <thead><tr><th v-for="t in title">{{t}}</th></tr></thead>
                <tbody :class="`tbody${type}`">
                    <tr v-for="d in tableData">
                        <td>
                            <span v-if="d.name ==='--'">--</span>
                            <a :href="'index' | detailLink({code:d.id})" v-else style="color:#1482F0" target="_blank">
                                {{d.name}}<span class="red-start" v-if="d.flag === '1'">*</span>
                            </a>
                        </td>
                        <td>{{d.money}}</td>
                        <td :style="d.profit | formatStyle">{{d.profit}}</td>
                    </tr>
                    <!--页面底部status：true:正在加载 false：没有更多数据-->
                    <bottom-status v-show="isLoading && hasMore" :status="true"></bottom-status>
                    <bottom-status v-show="!hasMore && !isEmpty && isFullScreen" :status="false"></bottom-status>
                </tbody>
            </table>
        </div>
        <!-- 初始化遮罩层 -->
        <div class="indicator" v-show="maskVisible">
            <div class="indicator-wrapper">
                <span class="indicator-spin"></span>
                <span class="indicator-text">正在加载...</span>
            </div>
            <div class="indicator-mask" @touchmove.stop.prevent  @scroll.stop.prevent></div>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../../components/noFound'
    import bottomStatus from '../../../components/bottomStatus';

    let params = common.getParams();

    export default {
        props: ['type'],
        data() {
            return {
                maskVisible: false,//是否显示遮罩
                tableData:[],//当前展示的数据列表
                title: [],//标题
                isLoading: false,//是否正在加载
                noPromise: false,
                bottomHeight:200,
                isEmpty: false,
                hasMore: false,//是否有更多数据
                isFullScreen: false //内容是否铺满屏幕，到达页面底部
            }
        },
        created () {
            this.loadData()
        },
        mounted() {
            // addEventListener('touchmove', this.onScroll.bind(this));
            $("#" + `mainContent${this.type}`).on('touchmove', () => {
                this.autoGet();
            })
        },
        methods: {
            // 初次加载，页面初始化
            loadData () {
                this.maskVisible = true;
                const req ={...params, skip: 0, pagesize: 15, type: this.type};
                this.$get('/finchinaAPP/nonStandard/getRelatedSecuritiesData.action', req).then(res => {
                    this.maskVisible = false;
                    this.hasMore = res.data.hasMore === 1;
                    this.title = res.data.title;
                    this.tableData = res.data.list;
                    if (this.hasMore){
                        this.autoGet();//数据未满屏，自动请求下一页
                    }else {
                        this.ifFullScreen();
                    }
                }).catch(err => {
                    this.maskVisible = false;
                    this.isEmpty = true;
                    this.hasMore = false;
                    this.isFullScreen = false;
                });
            },
            onScroll (e) {
                const isScrollBottom = common.getTbodyScrollBottom(`tbody${this.type}`) <= this.bottomHeight;
                if (this.hasMore && isScrollBottom && !this.isFullScreen && !this.isLoading) {
                    this.loadNest();
                }
            },
            // 请求下一页
            loadNest(){
                this.isLoading = true;
                const req ={...params, skip: this.tableData.length, pagesize: 15, type: this.type};
                this.$get('/finchinaAPP/nonStandard/getRelatedSecuritiesData.action', req).then(res => {
                    this.tableData.push(...res.data.list);
                    this.isLoading = false;
                    this.hasMore = res.data.hasMore === 1;
                    // 若内容未到底部，判断加载完是否到底部
                    this.ifFullScreen();
                }).catch(err=>{
                    this.hasMore = false;
                    this.isLoading = false;
                    // 若内容未到底部，判断加载完是否到底部
                    this.ifFullScreen();
                });
            },
            autoGet(){
                this.$nextTick(() => {
                    let t = document.querySelector("." + `tbody${this.type}`);
                    //滚动条滚到底部时：窗口高度+滚动条高度 = 内容区高度
                    let scrollTop = t.scrollTop,//滚动条高度
                        wHeight = t.clientHeight,//窗口高度
                        conHeight = t.scrollHeight;//内容高度

                    //在还有200px滚到底部之前加载下一页
                    let isScrollBottom = conHeight - wHeight - scrollTop <= this.bottomHeight;
                    if (this.hasMore && isScrollBottom && !this.isLoading) {
                        this.loadNest();
                    }
                })
            },
            //内容是否铺满屏幕
            ifFullScreen(){
                // 若内容未到底部，判断加载完是否到底部
                if (!this.maskVisible && !this.isFullScreen && !this.isEmpty){
                    this.$nextTick(() => {
                        //窗口高度
                        let wHeight = document.querySelector("#" + `mainContent${this.type}`).clientHeight;
                        if (wHeight > 0){
                            //内容高度
                            let conHeight = document.querySelector("." + `tbody${this.type}`).scrollHeight;
                            this.isFullScreen = wHeight - conHeight <= 150;
                        }
                    })
                }
            }
        },
        components: {
            noFound, bottomStatus
        }
    }
</script>

<style lang="less">
    @import "../../../assets/less/base";

    #app {
        .content{
            height: calc(100% - 0.63rem);
        }
        .main{
            height: 100%;
        }
        table{
            width: 100%;
            height: 100%;
            text-align: right;
            thead{
                font-family: PingFangSC-Medium;
                height: 0.96rem;
            }
            tbody{
                font-family: PingFangSC-Regular;
            }
            tr{
                width: calc(100% - 0.56rem);
                margin: 0 0.28rem;
            }
            th{
                font-size: 0.24rem;
                color: #333333;
                letter-spacing: 0;
                line-height: 0.24rem;

                padding: 0.28rem 0 0.28rem 0;
                &:first-child{
                     text-align: left;
                 }
            }
            td{
                font-size: 0.26rem;
                letter-spacing: 0;
                line-height: 0.26rem;
                color:  #111111;
                padding-bottom: 0.30rem;
                &:first-child{
                     text-align: left;
                }
            }
        }
        .red-start{
            color: red;
        }
        .bottom-msg {
            padding-top: 0.48rem;
        }
    }
    .indicator{
    .indicator-mask {
        top: 0.63rem;
        height: calc(100% - 0.63rem);
    }
    }
</style>