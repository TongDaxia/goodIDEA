export default {
    bind: function (el, binding, vnode) {
        let lastTouch = {};

        el.addEventListener('touchstart', e => {
            lastTouch = e.touches[0]
        });

        el.addEventListener('touchmove', (e) => {
            e.stopPropagation();
            let scrollEl = e.currentTarget,
                touch = e.touches[0];

            // 判定当前滚动容器是否可以滚动
            if ((scrollEl.scrollHeight <= scrollEl.offsetHeight) || (!scrollEl.scrollTop && touch.screenY > lastTouch.screenY) ||
                (scrollEl.scrollTop == scrollEl.scrollHeight - scrollEl.offsetHeight && touch.screenY < lastTouch.screenY)) {
                // 不能滚动的时候依然需要阻止滚动穿透
                e.preventDefault();//
            }
            lastTouch = touch;
        }, false);
    }
};