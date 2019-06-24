<template>
    <div id="app">
        <no-found v-if="isEmpty"></no-found>
        <div class="content">
            <div v-for="(d,i) in peopleList" class="people-box" :class="{'up': d.up}">
                <div class="people-text">
                    <div @click="upDown(d,i)">
                        <div class="photo">
                            <img v-if="d.image" :src="d.image"/>
                            <img src="../../assets/img/people.png" v-else/>
                        </div>
                        <div class="name-time">
                            <p class="name">{{d.name}}</p>
                            <p class="more-btn" :class="{'active': d.up}"></p>
                        </div>
                    </div>
                    <div class="text-content" ref="content" v-if="d.remark"
                         :class="{'active':d.showMore}">{{d.remark}}</div>
                        <p v-else style="margin-top: 0.2rem;margin-left: 0.1rem;">暂无人员说明</p>
                        <p class="more-btn"
                           v-show="d.hasMore"
                           :class="{'active':d.showMore,'up':d.up}"
                           @click="d.showMore = !d.showMore"></p>
                    </div>
                <div class="table-wrapper flex-row">
                    <div>
                        <table class="first" ref="tableLeft">
                            <tr><th>产品名称</th></tr>
                            <tr v-for="v in d.productList">
                                <td>
                                    <span v-if="v.productName === '--'">--</span>
                                    <a :href="'index' | detailLink({code:v.productCode})" v-else style="color:#1482F0" target="_blank">
                                        {{v.productName}}
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="auto" ref="autoTab">
                        <div class="auto-box">
                            <table class="second" ref="tableRight">
                                <tr>
                                    <th>任职日期</th>
                                    <th>离职日期</th>
                                    <th>任期回报(%)</th>
                                </tr>
                                <tr v-for="v in d.productList">
                                    <td>{{v.productStartDate}}</td>
                                    <td>{{v.productEndDate}}</td>
                                    <td :style="v.returnOnTenure | formatStyle">{{v.returnOnTenure}}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="pagination" v-if="d.productCount > 5">
                    <p>
                        <span class="first" :class="{'disabled':d.curPage === 1}" @click="changePage($event,d,i)">首页</span>
                        <span class="prev" :class="{'disabled':d.curPage === 1}" @click="changePage($event,d,i)">上一页</span>
                        <span class="num">{{d.curPage}}/{{d.totalPage}}</span>
                        <span class="next" :class="{'disabled':d.curPage === d.totalPage}" @click="changePage($event,d,i)">下一页</span>
                        <span class="last" :class="{'disabled':d.curPage === d.totalPage}" @click="changePage($event,d,i)">末页</span>
                    </p>
                </div>
            </div>
            <!--页面底部status：true:正在加载 false：没有更多数据-->
            <!--<div class="more-people" v-if="loadPeopleBtn" @click="loadMorePeople">点击加载更多经理信息</div>-->
            <bottom-status v-show="isLoading && hasMore" :status="true"></bottom-status>
            <bottom-status v-show="!hasMore && !isEmpty && isFullScreen" :status="false"></bottom-status>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../components/noFound';
    import bottomStatus from '../../components/bottomStatus';
    let param = common.getParams();

    export default {
        data() {
            return {
                peopleList:[],
                isLoading: false,
                isEmpty: false,
                isFullScreen: false,   //内容是否铺满屏幕，到达页面底部
                hasMore: false, //是否有更多信息
                bottomHeight: 200,
            }
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();
            this.$nextTick(() => {
                common.addWaterMark();
            });
            this.init();
            addEventListener('touchmove', this.onScroll.bind());
        },
        methods: {
            //页面初始化
            init(){
                this.$loading.open();
                // pagesize：投资经理首次加载数量，childsize：投资经理产品首次加载数量
                const req = {...param, pagesize: 3, skip: 0, childsize: 5};
                this.$get('/finchinaAPP/nonStandard/getManagerDetailList.action', req).then((res) => {
                    this.$loading.close();
                    res.data.map(d =>{
                        d.curPage = 1;
                        d.totalPage = Math.ceil(d.productCount / 5);
                    });
                    this.peopleList = res.data;
                    if (param.total > this.peopleList.length){
                        this.hasMore = true;
                    }
                    this.textSlide();
                    this.initTableLayout(0,res.data.length-1);
                    // 若内容未到底部，判断加载完是否到底部
                    if (!this.isFullScreen){
                        this.ifFullScreen();
                    }
                }).catch(err => {
                    this.$loading.close();
                    this.isEmpty = true;
                    this.hasMore = false;
                    this.isFullScreen = false;
                });
            },
            //加载更多经理
            loadMorePeople(){
                this.isLoading = true;
                // pagesize：投资经理首次加载数量，childsize：投资经理产品首次加载数量
                const req = {...param, pagesize: 1, skip: this.peopleList.length, childsize: 5};
                this.$get('/finchinaAPP/nonStandard/getManagerDetailList.action', req).then(res => {
                    res.data.map(d =>{
                        d.curPage = 1;
                        d.totalPage = Math.ceil(d.productCount / 5);
                    });
                    this.peopleList = this.peopleList.concat(res.data);
                    if (param.total > this.peopleList.length){
                        this.hasMore = true;
                    }
                    this.textSlide();
                    const len =  this.peopleList.length;
                    this.initTableLayout(len-1, len-1);
                    this.isLoading = false;
                    // 若内容未到底部，判断加载完是否到底部
                    if (!this.isFullScreen){
                        this.ifFullScreen();
                    }
                }).catch(err=>{
                    this.hasMore = false;
                    this.isLoading = false;
                    // 若内容未到底部，判断加载完是否到底部
                    if (!this.isFullScreen){
                        this.ifFullScreen();
                    }
                });
            },
            //滚动加载方法
            onScroll () {
                if (this.hasMore && !this.isLoading){
                    const isScrollBottom = common.getScrollBottomHeight('.content') <= this.bottomHeight;
                    if (isScrollBottom) {
                        this.loadMorePeople();
                    }
                }
            },
            //经理描述超出处理
            textSlide(){
                this.$nextTick(()=>{
                    if (this.$refs.content){
                        this.$refs.content.map((v,i) =>{
                            if(v.scrollHeight > v.clientHeight){
                                this.$set(this.peopleList[i],'hasMore',true);
                                this.$set(this.peopleList[i],'showMore',false);
                            }else{
                                v.style.height = "auto";
                            }
                        })
                    }
                })
            },
            //表格高度统一
            initTableLayout (begin,end) {
                this.$nextTick(()=>{
                    for (let i = begin; i <= end; i++) {
                        const leftTr = this.$refs.tableLeft[i].getElementsByTagName('tr'),//表格左一列
                            rightTr = this.$refs.tableRight[i].getElementsByTagName('tr');//表格右三列
                        for (let j = 0,l = leftTr.length; j < l; j++) {
                            rightTr[j].style.height = leftTr[j].clientHeight +'px';
                        }
                        this.$refs.autoTab[i].style.height = this.$refs.tableRight[i].clientHeight - 7 +'px';
                    }
                })
            },
            //经理展开收起
            upDown(d,i){
                this.$set(this.peopleList[i],'up',!d.up);
                this.$nextTick(()=>{
                    this.onScroll();
                })
            },
            //分页按钮点击
            changePage(e,d,i){
                const c = e.target.className || '';
                if(c.includes('disabled')) return false;
                if(c.includes('first')) d.curPage = 1;
                if(c.includes('last')) d.curPage = d.totalPage;
                if(c.includes('prev')) d.curPage -= 1;
                if(c.includes('next')) d.curPage += 1;
                this.loadMoreProduct(d,i);
            },
            //产品分页
            loadMoreProduct(d,i){
                const req ={...param, code: d.id, skip: 5 * (d.curPage - 1), pagesize: 5};
                this.$get('/finchinaAPP/nonStandard/getManagerProductList.action', req).then(res => {
                    d.productList = res.data;
                    this.initTableLayout(i, i);
                })
            },
            //内容是否铺满屏幕
            ifFullScreen(){
                this.$nextTick(() => {
                    //内容区高度 > 窗口高度 表示窗口已铺满
                    let wHeight = document.documentElement.clientHeight,//窗口高度
                        conHeight = document.querySelector('.content').scrollHeight;//内容高度
                    this.isFullScreen = wHeight - conHeight <= 100;
                })
            }
        },
        computed:{
            loadPeopleBtn(){
                return this.peopleList.length && param.total > this.peopleList.length && !this.isLoading;
            },
        },
        components: {noFound, bottomStatus},
    }
</script>

<style lang="less">
    @import "../../assets/less/base";

    #app {
        min-height: 100%;
        background: #fff;
        .content{
            background: #F6F6F6;
        }
        .more-people{
            color: #1482f0;
            text-align: center;
            padding: 0.3rem 0;
        }
        .people-box{
            background: #fff;
            margin-bottom: 0.16rem;
            &.up{
                height: 1.8rem;
                overflow: hidden;
            }
            .people-text{
                padding: 0.28rem 0.32rem 0 0.32rem;
                .photo{
                    display: inline-block;
                    width: 1.28rem;
                    height: 1.28rem;
                    border-radius: 50%;
                    overflow: hidden;
                    img{
                        width: 100%;
                    }
                }
                .name-time{
                    display: inline-block;
                    vertical-align: top;
                    margin: 0.4rem 0 0.24rem 0.4rem;
                    width: calc(100% - 1.92rem);
                    p{
                        margin: 0;
                        &.name{
                            font-size: 0.3rem;
                            color: #333333;
                            font-weight: bold;
                            float: left;
                        }
                        &.more-btn{
                            float: right;
                            &.active:before {
                                margin-top: 0.15rem;
                                transform: rotate(225deg);
                            }
                        }
                    }
                }
                .text-content{
                    font-size: 0.24rem;
                    color: #5C5C5C;
                    line-height: 0.44rem;
                    height: 1.32rem;
                    overflow: hidden;
                    margin: 0.3rem 0 0.2rem 0;
                    &.active{
                        height: auto;
                    }
                }
                .more-btn{
                    text-align: center;
                    padding: 0 0 0.4rem 0;
                    &.up:before{
                        display: none;
                    }
                    &:before{
                        content: "";
                        position: absolute;
                        display: inline-block;
                        width: 0.15rem;
                        height: 0.15rem;
                        border: 1px solid;
                        border-color: transparent #8C8C8C #8C8C8C transparent;
                        transform: rotate(45deg);
                        transition: all 0.3s;
                    }
                    &.active:before {
                        transform: rotate(225deg);
                    }
                }
            }
            .table-wrapper{
                padding: 0.24rem 0.32rem 0;
                position: relative;
                &:before,&:after{
                    content: '';
                    top:0.35rem;
                    position: absolute;
                    display: inline-block;
                    border: 0.1rem solid transparent;
                }
                &:before{
                    border-right-color: #8c8c8c;
                    left: 3.3rem;
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
            .pagination{
                text-align: right;
                padding: 0.2rem 0.32rem 0.24rem 0.32rem;
                span{
                    font-family: PingFangSC-Regular;
                    font-size: 0.24rem;
                    margin-left: 0.2rem;
                    color: #1482f0;
                    &.num{
                        font-family: Helvetica;
                        font-size: 0.24rem;
                        color: #3C3C3C;
                    }
                    &.disabled{
                        color: #8c8c8c;
                    }
                }
            }
            table{
                table-layout: fixed;
                width: 100%;
                text-align: right;
                th{
                    font-family: PingFangSC-Medium;
                    font-size: 0.28rem;
                    color: #333333;
                    padding-bottom: 0.36rem;
                }
                td{
                    font-family: PingFangSC-Regular;
                    font-size: 0.26rem;
                    color: #3C3C3C;
                    padding-bottom: 0.36rem;
                }
                &.first{
                    width: 3.4rem;
                    th,td{text-align: left;}
                }
                &.second{
                    th,td{text-align: right;width: 1.7rem;}
                }
            }
            .product-text{
                background: #fff;
                padding: 0 0.32rem 0.32rem 0.32rem;
                pointer-events: none;
                .more-msg{
                    text-align: center;
                    pointer-events: auto;
                }
                .no-more-msg{
                    text-align: center;
                    margin:0 auto;
                }
            }
        }
    }
</style>
