<template>
    <div class="content" id="app">
        <no-found v-show="isEmpty" type="0"></no-found>
        <div id="itemBox" class="item-box">
            <div class="item" v-for="item in data">
                <div class="item-top">
                    <p class="news">
                        <a :href="announceItemLink(item)" target="_blank">{{item.title}}</a>
                    </p>
                    <span class="warn" v-if="item.risk" :style="'background-color:'+item['color']">
                    {{item['risk']}}
                </span>
                </div>
                <div class="item-bottom">
                    <p class="time">{{item.publishDate | formatDate}}</p>
                    <p class="start-share">
                        <!--<img src="../../assets/img/start-light.png" v-if="d.collection"/>-->
                        <!--<img src="../../assets/img/start.png" v-else/>-->
                        <!--<a :href="">-->
                            <img src="../../assets/img/share.png" @click="shareLink(item)"/>
                        <!--</a>-->
                    </p>
                </div>
            </div>
        </div>
        <!--页面底部status：true:正在加载 false：没有更多数据 -->
        <template v-if="isFullScreen">
            <bottom-status v-show="isLoading && hasNest" :status="true"></bottom-status>
            <bottom-status v-show="!hasNest && !isEmpty" :status="false"></bottom-status>
        </template>
    </div>
</template>

<script>
    import noFound from '../../components/noFound';
    import common from '@utils/commons';
    import bottomStatus from '../../components/bottomStatus';

    let _ = require('lodash');
    let param = common.getParams();
    export default {
        data() {
            return {
                data: [],
                isLoading: false,
                bottomHeight: 200,
                isEmpty: false,
                hasNest: true,
                isFullScreen:false,
            }
        },
        created() {
            this.loadData()
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();
            this.$nextTick(() => {
                common.addWaterMark();
            });
            addEventListener('touchmove', this.onScroll.bind(this))
        },
        methods: {
            loadData() {
                this.$loading.open();
                const req = {...param, type:'detail'};
                this.$get('/finchinaAPP/nonStandard/getNotice.action', req).then(res => {
                    this.$loading.close();
                    this.data = res.data.list;
                    this.hasNest = res.data.hasMore === 1;
                    this.$nextTick(()=>{
                        const flag = common.getScrollBottomHeight('#itemBox') >=0;
                        if(flag){
                            this.isFullScreen = true;
                        }
                    })
                }).catch(err => {
                    this.$loading.close();
                    this.isEmpty = true;
                    this.hasNest = false;
                });
            },
            onScroll() {
                const isScrollBottom = common.getScrollBottomHeight() <= this.bottomHeight;
                if (isScrollBottom && this.hasNest && !this.isLoading) {
                    this.isLoading = true;
                    const req = {...param, type:'detail',pagesize: 15 ,skip: this.data.length};
                    this.$get('/finchinaAPP/nonStandard/getNotice.action', req).then(res => {
                        this.isLoading = false;
                        this.data = this.data.concat(res.data.list);
                        this.hasNest = res.data.hasMore === 1;
                    }).catch(err => {
                        this.hasNest = false;
                        this.isLoading = false;
                    });
                }
            },
            announceItemLink(item) {
                return common.resolveUrl({
                    path: location.origin +'/finchinaAPP/newsDetail.html',
                    queries: {
                        id: item['id'],
                        type: item['type'],
                        user: param.user
                    }
                })
            },
            //公告分享
            async shareLink(item){
                try {
                    const req = {...param, type:'notice_trust',id:item.id};
                    const {data} = await this.$get('/finchinaAPP/getShareInfoDetail.action', req);
                    const url = common.resolveUrl({
                        path: '/finchinaAPP/newsDetail.html',
                        queries: {
                            id: item['id'],
                            type: item['type'],
                            user: param.user
                        }
                    });
                    const src = common.resolveUrl({
                        path: 'dzh://shareActivity',
                        queries: {
                            url:`${location.origin}${url}&sharetype=1`,
                            image:location.origin + '/finchinaAPP/static/logo.png',
                            title:data.title,
                            content:data.content,
                        }
                    });
                    location = src;
                } catch (err) {}
            }
        },
        components: {noFound, bottomStatus},
    };
</script>

<style lang="less">
    @import "../../assets/less/base";
    .content {
        .item-box{
            padding: 0 0.32rem;
        }
        .item{
            width: 100%;
            padding-top: 0.24rem;
            &:last-child{
                .item-bottom {
                    border-bottom: 0;
                }
            }
            .item-top {
                display: flex;
                align-items: baseline;
                .news {
                    flex: 1;
                    font-size: 0.3rem;
                    line-height: 0.42rem;
                    color: #000;
                    .type{
                        &:after{
                            content: '';
                            width: 0.04rem;
                            height: 0.28rem;
                            background: #000;
                            display: inline-block;
                            vertical-align: top;
                            margin: 0.06rem 0.16rem 0 0.16rem;
                        }
                    }
                }
                .warn {
                    background: #F78182;
                    color: #fff;
                    margin-left: 0.15rem;
                    font-size: 0.22rem;
                    line-height: 0.36rem;
                    text-align: center;
                    padding: 0 0.08rem;
                    border-radius: 0.02rem;
                }
            }
            .item-bottom {
                display: flex;
                justify-content: space-between;
                margin-top: 0.2rem;
                border-bottom: 0.01rem solid #E6E6E6;
                padding-bottom: 0.22rem;
                .time {
                    line-height: 0.32rem;
                    font-size: 0.22rem;
                    color: #78828D;
                }
                .start-share {
                    /*margin-left: 0.4rem;*/
                    img {
                        display: inline-block;
                        width: 0.32rem;
                        height: 0.32rem;
                    }
                }
            }
        }
        .bottom-msg {
            padding-top: 0.48rem;
        }
    }
</style>
