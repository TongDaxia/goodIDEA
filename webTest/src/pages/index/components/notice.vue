<template>
    <div class="notice pannel">
        <div class="head-title">
            <a :href="'notice_detail' | detailLink" target="_blank" class="btn-detail"></a>
            <div class="title-box">
                <span class="title">产品公告</span>
                <span class="btn"></span>
            </div>
        </div>
        <div class="notice-content" v-if="notice.length">
            <div class="item" v-for="d in notice">
                <div class="item-top">
                    <p class="news">
                        <a :href="noticeLink(d)" target="_blank">{{d.title}}</a>
                    </p>
                    <span class="warn" v-if="d.risk" :style="'background-color:'+d.color">{{d.risk}}</span>
                </div>
                <div class="item-bottom">
                    <p class="time">{{d.publishDate | formatDate}}</p>
                </div>
            </div>
        </div>
        <p class="no-data" v-else>暂无相关信息</p>
    </div>
</template>

<script>
    import common from '@utils/commons';
    const param = common.getParams();

    export default {
        data() {
            return {
                notice:{},
            }
        },
        created(){

        },
        methods: {
            loadData(){
                const req ={...param, type:'outline'};
                this.$get('/finchinaAPP/nonStandard/getNotice.action', req).then(res => {
                    this.notice = res.data.list;
                })
            },
            /**
             * 跳转公告详情
             */
            noticeLink(d){
                return common.resolveUrl({
                    path: location.origin +'/finchinaAPP/newsDetail.html',
                    queries: {
                        id: d['id'],
                        type: d['type'],
                        user: common.getParams().user
                    }
                });
            },
        }
    }
</script>

<style scoped lang="less">
    .notice-content {
        padding: 0 0.32rem;
        border-top: 0.01rem solid #E6E6E6;
        margin-top: 0.3rem;
        .item{
            padding-top: 0.24rem;
            &:last-child{
                .item-bottom {
                    border-bottom: 0;
                    padding-bottom: 0;
                }
            }
        }
        .item-top {
            display: flex;
            align-items: baseline;
        }
        .item-bottom {
            display: flex;
            justify-content: space-between;
            margin-top: 0.2rem;
            border-bottom: 0.01rem solid #E6E6E6;
            padding-bottom: 0.22rem;
        }
        .news {
            flex: 1;
            font-size: 0.3rem;
            line-height: 0.42rem;
            color: #000;
            /*font-weight: 600;*/
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
</style>