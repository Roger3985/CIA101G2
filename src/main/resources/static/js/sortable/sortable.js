/*! Sortable 1.15.2 - MIT | git://github.com/SortableJS/Sortable.git */
!function (t, e) {
    "object" == typeof exports && "undefined" != typeof module ? module.exports = e() : "function" == typeof define && define.amd ? define(e) : (t = t || self).Sortable = e()
}(this, function () {
    "use strict";

    function e(e, t) {
        var n, o = Object.keys(e);
        return Object.getOwnPropertySymbols && (n = Object.getOwnPropertySymbols(e), t && (n = n.filter(function (t) {
            return Object.getOwnPropertyDescriptor(e, t).enumerable
        })), o.push.apply(o, n)), o
    }

    function I(o) {
        for (var t = 1; t < arguments.length; t++) {
            var i = null != arguments[t] ? arguments[t] : {};
            t % 2 ? e(Object(i), !0).forEach(function (t) {
                var e, n;
                e = o, t = i[n = t], n in e ? Object.defineProperty(e, n, {
                    value: t,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[n] = t
            }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(o, Object.getOwnPropertyDescriptors(i)) : e(Object(i)).forEach(function (t) {
                Object.defineProperty(o, t, Object.getOwnPropertyDescriptor(i, t))
            })
        }
        return o
    }

    function o(t) {
        return (o = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (t) {
            return typeof t
        } : function (t) {
            return t && "function" == typeof Symbol && t.constructor === Symbol && t !== Symbol.prototype ? "symbol" : typeof t
        })(t)
    }

    function a() {
        return (a = Object.assign || function (t) {
            for (var e = 1; e < arguments.length; e++) {
                var n, o = arguments[e];
                for (n in o) Object.prototype.hasOwnProperty.call(o, n) && (t[n] = o[n])
            }
            return t
        }).apply(this, arguments)
    }

    function i(t, e) {
        if (null == t) return {};
        var n, o = function (t, e) {
            if (null == t) return {};
            for (var n, o = {}, i = Object.keys(t), r = 0; r < i.length; r++) n = i[r], 0 <= e.indexOf(n) || (o[n] = t[n]);
            return o
        }(t, e);
        if (Object.getOwnPropertySymbols) for (var i = Object.getOwnPropertySymbols(t), r = 0; r < i.length; r++) n = i[r], 0 <= e.indexOf(n) || Object.prototype.propertyIsEnumerable.call(t, n) && (o[n] = t[n]);
        return o
    }

    function r(t) {
        return function (t) {
            if (Array.isArray(t)) return l(t)
        }(t) || function (t) {
            if ("undefined" != typeof Symbol && null != t[Symbol.iterator] || null != t["@@iterator"]) return Array.from(t)
        }(t) || function (t, e) {
            if (t) {
                if ("string" == typeof t) return l(t, e);
                var n = Object.prototype.toString.call(t).slice(8, -1);
                return "Map" === (n = "Object" === n && t.constructor ? t.constructor.name : n) || "Set" === n ? Array.from(t) : "Arguments" === n || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n) ? l(t, e) : void 0
            }
        }(t) || function () {
            throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")
        }()
    }

    function l(t, e) {
        (null == e || e > t.length) && (e = t.length);
        for (var n = 0, o = new Array(e); n < e; n++) o[n] = t[n];
        return o
    }

    function t(t) {
        if ("undefined" != typeof window && window.navigator) return !!navigator.userAgent.match(t)
    }

    var y = t(/(?:Trident.*rv[ :]?11\.|msie|iemobile|Windows Phone)/i), w = t(/Edge/i), s = t(/firefox/i),
        u = t(/safari/i) && !t(/chrome/i) && !t(/android/i), n = t(/iP(ad|od|hone)/i),
        c = t(/chrome/i) && t(/android/i), d = {capture: !1, passive: !1};

    function h(t, e, n) {
        t.addEventListener(e, n, !y && d)
    }

    function f(t, e, n) {
        t.removeEventListener(e, n, !y && d)
    }

    function p(t, e) {
        if (e && (">" === e[0] && (e = e.substring(1)), t)) try {
            if (t.matches) return t.matches(e);
            if (t.msMatchesSelector) return t.msMatchesSelector(e);
            if (t.webkitMatchesSelector) return t.webkitMatchesSelector(e)
        } catch (t) {
            return
        }
    }

    function P(t, e, n, o) {
        if (t) {
            n = n || document;
            do {
                if (null != e && (">" !== e[0] || t.parentNode === n) && p(t, e) || o && t === n) return t
            } while (t !== n && (t = (i = t).host && i !== document && i.host.nodeType ? i.host : i.parentNode))
        }
        var i;
        return null
    }

    var g, m = /\s+/g;

    function k(t, e, n) {
        var o;
        t && e && (t.classList ? t.classList[n ? "add" : "remove"](e) : (o = (" " + t.className + " ").replace(m, " ").replace(" " + e + " ", " "), t.className = (o + (n ? " " + e : "")).replace(m, " ")))
    }

    function R(t, e, n) {
        var o = t && t.style;
        if (o) {
            if (void 0 === n) return document.defaultView && document.defaultView.getComputedStyle ? n = document.defaultView.getComputedStyle(t, "") : t.currentStyle && (n = t.currentStyle), void 0 === e ? n : n[e];
            o[e = !(e in o || -1 !== e.indexOf("webkit")) ? "-webkit-" + e : e] = n + ("string" == typeof n ? "" : "px")
        }
    }

    function v(t, e) {
        var n = "";
        if ("string" == typeof t) n = t; else do {
            var o = R(t, "transform")
        } while (o && "none" !== o && (n = o + " " + n), !e && (t = t.parentNode));
        var i = window.DOMMatrix || window.WebKitCSSMatrix || window.CSSMatrix || window.MSCSSMatrix;
        return i && new i(n)
    }

    function b(t, e, n) {
        if (t) {
            var o = t.getElementsByTagName(e), i = 0, r = o.length;
            if (n) for (; i < r; i++) n(o[i], i);
            return o
        }
        return []
    }

    function O() {
        var t = document.scrollingElement;
        return t || document.documentElement
    }

    function X(t, e, n, o, i) {
        if (t.getBoundingClientRect || t === window) {
            var r, a, l, s, c, u,
                d = t !== window && t.parentNode && t !== O() ? (a = (r = t.getBoundingClientRect()).top, l = r.left, s = r.bottom, c = r.right, u = r.height, r.width) : (l = a = 0, s = window.innerHeight, c = window.innerWidth, u = window.innerHeight, window.innerWidth);
            if ((e || n) && t !== window && (i = i || t.parentNode, !y)) do {
                if (i && i.getBoundingClientRect && ("none" !== R(i, "transform") || n && "static" !== R(i, "position"))) {
                    var h = i.getBoundingClientRect();
                    a -= h.top + parseInt(R(i, "border-top-width")), l -= h.left + parseInt(R(i, "border-left-width")), s = a + r.height, c = l + r.width;
                    break
                }
            } while (i = i.parentNode);
            return o && t !== window && (o = (e = v(i || t)) && e.a, t = e && e.d, e && (s = (a /= t) + (u /= t), c = (l /= o) + (d /= o))), {
                top: a,
                left: l,
                bottom: s,
                right: c,
                width: d,
                height: u
            }
        }
    }

    function Y(t, e, n) {
        for (var o = M(t, !0), i = X(t)[e]; o;) {
            var r = X(o)[n];
            if (!("top" === n || "left" === n ? r <= i : i <= r)) return o;
            if (o === O()) break;
            o = M(o, !1)
        }
        return !1
    }

    function B(t, e, n, o) {
        for (var i = 0, r = 0, a = t.children; r < a.length;) {
            if ("none" !== a[r].style.display && a[r] !== Ft.ghost && (o || a[r] !== Ft.dragged) && P(a[r], n.draggable, t, !1)) {
                if (i === e) return a[r];
                i++
            }
            r++
        }
        return null
    }

    function F(t, e) {
        for (var n = t.lastElementChild; n && (n === Ft.ghost || "none" === R(n, "display") || e && !p(n, e));) n = n.previousElementSibling;
        return n || null
    }

    function j(t, e) {
        var n = 0;
        if (!t || !t.parentNode) return -1;
        for (; t = t.previousElementSibling;) "TEMPLATE" === t.nodeName.toUpperCase() || t === Ft.clone || e && !p(t, e) || n++;
        return n
    }

    function E(t) {
        var e = 0, n = 0, o = O();
        if (t) do {
            var i = v(t), r = i.a, i = i.d
        } while (e += t.scrollLeft * r, n += t.scrollTop * i, t !== o && (t = t.parentNode));
        return [e, n]
    }

    function M(t, e) {
        if (!t || !t.getBoundingClientRect) return O();
        var n = t, o = !1;
        do {
            if (n.clientWidth < n.scrollWidth || n.clientHeight < n.scrollHeight) {
                var i = R(n);
                if (n.clientWidth < n.scrollWidth && ("auto" == i.overflowX || "scroll" == i.overflowX) || n.clientHeight < n.scrollHeight && ("auto" == i.overflowY || "scroll" == i.overflowY)) {
                    if (!n.getBoundingClientRect || n === document.body) return O();
                    if (o || e) return n;
                    o = !0
                }
            }
        } while (n = n.parentNode);
        return O()
    }

    function D(t, e) {
        return Math.round(t.top) === Math.round(e.top) && Math.round(t.left) === Math.round(e.left) && Math.round(t.height) === Math.round(e.height) && Math.round(t.width) === Math.round(e.width)
    }

    function S(e, n) {
        return function () {
            var t;
            g || (1 === (t = arguments).length ? e.call(this, t[0]) : e.apply(this, t), g = setTimeout(function () {
                g = void 0
            }, n))
        }
    }

    function H(t, e, n) {
        t.scrollLeft += e, t.scrollTop += n
    }

    function _(t) {
        var e = window.Polymer, n = window.jQuery || window.Zepto;
        return e && e.dom ? e.dom(t).cloneNode(!0) : n ? n(t).clone(!0)[0] : t.cloneNode(!0)
    }

    function C(t, e) {
        R(t, "position", "absolute"), R(t, "top", e.top), R(t, "left", e.left), R(t, "width", e.width), R(t, "height", e.height)
    }

    function T(t) {
        R(t, "position", ""), R(t, "top", ""), R(t, "left", ""), R(t, "width", ""), R(t, "height", "")
    }

    function L(n, o, i) {
        var r = {};
        return Array.from(n.children).forEach(function (t) {
            var e;
            P(t, o.draggable, n, !1) && !t.animated && t !== i && (e = X(t), r.left = Math.min(null !== (t = r.left) && void 0 !== t ? t : 1 / 0, e.left), r.top = Math.min(null !== (t = r.top) && void 0 !== t ? t : 1 / 0, e.top), r.right = Math.max(null !== (t = r.right) && void 0 !== t ? t : -1 / 0, e.right), r.bottom = Math.max(null !== (t = r.bottom) && void 0 !== t ? t : -1 / 0, e.bottom))
        }), r.width = r.right - r.left, r.height = r.bottom - r.top, r.x = r.left, r.y = r.top, r
    }

    var K = "Sortable" + (new Date).getTime();

    function x() {
        var e, o = [];
        return {
            captureAnimationState: function () {
                o = [], this.options.animation && [].slice.call(this.el.children).forEach(function (t) {
                    var e, n;
                    "none" !== R(t, "display") && t !== Ft.ghost && (o.push({
                        target: t,
                        rect: X(t)
                    }), e = I({}, o[o.length - 1].rect), !t.thisAnimationDuration || (n = v(t, !0)) && (e.top -= n.f, e.left -= n.e), t.fromRect = e)
                })
            }, addAnimationState: function (t) {
                o.push(t)
            }, removeAnimationState: function (t) {
                o.splice(function (t, e) {
                    for (var n in t) if (t.hasOwnProperty(n)) for (var o in e) if (e.hasOwnProperty(o) && e[o] === t[n][o]) return Number(n);
                    return -1
                }(o, {target: t}), 1)
            }, animateAll: function (t) {
                var c = this;
                if (!this.options.animation) return clearTimeout(e), void ("function" == typeof t && t());
                var u = !1, d = 0;
                o.forEach(function (t) {
                    var e = 0, n = t.target, o = n.fromRect, i = X(n), r = n.prevFromRect, a = n.prevToRect, l = t.rect,
                        s = v(n, !0);
                    s && (i.top -= s.f, i.left -= s.e), n.toRect = i, n.thisAnimationDuration && D(r, i) && !D(o, i) && (l.top - i.top) / (l.left - i.left) == (o.top - i.top) / (o.left - i.left) && (t = l, s = r, r = a, a = c.options, e = Math.sqrt(Math.pow(s.top - t.top, 2) + Math.pow(s.left - t.left, 2)) / Math.sqrt(Math.pow(s.top - r.top, 2) + Math.pow(s.left - r.left, 2)) * a.animation), D(i, o) || (n.prevFromRect = o, n.prevToRect = i, e = e || c.options.animation, c.animate(n, l, i, e)), e && (u = !0, d = Math.max(d, e), clearTimeout(n.animationResetTimer), n.animationResetTimer = setTimeout(function () {
                        n.animationTime = 0, n.prevFromRect = null, n.fromRect = null, n.prevToRect = null, n.thisAnimationDuration = null
                    }, e), n.thisAnimationDuration = e)
                }), clearTimeout(e), u ? e = setTimeout(function () {
                    "function" == typeof t && t()
                }, d) : "function" == typeof t && t(), o = []
            }, animate: function (t, e, n, o) {
                var i, r;
                o && (R(t, "transition", ""), R(t, "transform", ""), i = (r = v(this.el)) && r.a, r = r && r.d, i = (e.left - n.left) / (i || 1), r = (e.top - n.top) / (r || 1), t.animatingX = !!i, t.animatingY = !!r, R(t, "transform", "translate3d(" + i + "px," + r + "px,0)"), this.forRepaintDummy = t.offsetWidth, R(t, "transition", "transform " + o + "ms" + (this.options.easing ? " " + this.options.easing : "")), R(t, "transform", "translate3d(0,0,0)"), "number" == typeof t.animated && clearTimeout(t.animated), t.animated = setTimeout(function () {
                    R(t, "transition", ""), R(t, "transform", ""), t.animated = !1, t.animatingX = !1, t.animatingY = !1
                }, o))
            }
        }
    }

    var A = [], N = {initializeByDefault: !0}, W = {
        mount: function (e) {
            for (var t in N) !N.hasOwnProperty(t) || t in e || (e[t] = N[t]);
            A.forEach(function (t) {
                if (t.pluginName === e.pluginName) throw "Sortable: Cannot mount plugin ".concat(e.pluginName, " more than once")
            }), A.push(e)
        }, pluginEvent: function (e, n, o) {
            var t = this;
            this.eventCanceled = !1, o.cancel = function () {
                t.eventCanceled = !0
            };
            var i = e + "Global";
            A.forEach(function (t) {
                n[t.pluginName] && (n[t.pluginName][i] && n[t.pluginName][i](I({sortable: n}, o)), n.options[t.pluginName] && n[t.pluginName][e] && n[t.pluginName][e](I({sortable: n}, o)))
            })
        }, initializePlugins: function (n, o, i, t) {
            for (var e in A.forEach(function (t) {
                var e = t.pluginName;
                (n.options[e] || t.initializeByDefault) && ((t = new t(n, o, n.options)).sortable = n, t.options = n.options, n[e] = t, a(i, t.defaults))
            }), n.options) {
                var r;
                n.options.hasOwnProperty(e) && (void 0 !== (r = this.modifyOption(n, e, n.options[e])) && (n.options[e] = r))
            }
        }, getEventProperties: function (e, n) {
            var o = {};
            return A.forEach(function (t) {
                "function" == typeof t.eventProperties && a(o, t.eventProperties.call(n[t.pluginName], e))
            }), o
        }, modifyOption: function (e, n, o) {
            var i;
            return A.forEach(function (t) {
                e[t.pluginName] && t.optionListeners && "function" == typeof t.optionListeners[n] && (i = t.optionListeners[n].call(e[t.pluginName], o))
            }), i
        }
    };

    function z(t) {
        var e = t.sortable, n = t.rootEl, o = t.name, i = t.targetEl, r = t.cloneEl, a = t.toEl, l = t.fromEl,
            s = t.oldIndex, c = t.newIndex, u = t.oldDraggableIndex, d = t.newDraggableIndex, h = t.originalEvent,
            f = t.putSortable, p = t.extraEventProperties;
        if (e = e || n && n[K]) {
            var g, m = e.options, t = "on" + o.charAt(0).toUpperCase() + o.substr(1);
            !window.CustomEvent || y || w ? (g = document.createEvent("Event")).initEvent(o, !0, !0) : g = new CustomEvent(o, {
                bubbles: !0,
                cancelable: !0
            }), g.to = a || n, g.from = l || n, g.item = i || n, g.clone = r, g.oldIndex = s, g.newIndex = c, g.oldDraggableIndex = u, g.newDraggableIndex = d, g.originalEvent = h, g.pullMode = f ? f.lastPutMode : void 0;
            var v, b = I(I({}, p), W.getEventProperties(o, e));
            for (v in b) g[v] = b[v];
            n && n.dispatchEvent(g), m[t] && m[t].call(e, g)
        }
    }

    function G(t, e) {
        var n = (o = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : {}).evt, o = i(o, U);
        W.pluginEvent.bind(Ft)(t, e, I({
            dragEl: V,
            parentEl: Z,
            ghostEl: $,
            rootEl: Q,
            nextEl: J,
            lastDownEl: tt,
            cloneEl: et,
            cloneHidden: nt,
            dragStarted: gt,
            putSortable: st,
            activeSortable: Ft.active,
            originalEvent: n,
            oldIndex: ot,
            oldDraggableIndex: rt,
            newIndex: it,
            newDraggableIndex: at,
            hideGhostForTarget: Rt,
            unhideGhostForTarget: Xt,
            cloneNowHidden: function () {
                nt = !0
            },
            cloneNowShown: function () {
                nt = !1
            },
            dispatchSortableEvent: function (t) {
                q({sortable: e, name: t, originalEvent: n})
            }
        }, o))
    }

    var U = ["evt"];

    function q(t) {
        z(I({
            putSortable: st,
            cloneEl: et,
            targetEl: V,
            rootEl: Q,
            oldIndex: ot,
            oldDraggableIndex: rt,
            newIndex: it,
            newDraggableIndex: at
        }, t))
    }

    var V, Z, $, Q, J, tt, et, nt, ot, it, rt, at, lt, st, ct, ut, dt, ht, ft, pt, gt, mt, vt, bt, yt, wt = !1, Et = !1,
        Dt = [], St = !1, _t = !1, Ct = [], Tt = !1, xt = [], Ot = "undefined" != typeof document, Mt = n,
        At = w || y ? "cssFloat" : "float", Nt = Ot && !c && !n && "draggable" in document.createElement("div"),
        It = function () {
            if (Ot) {
                if (y) return !1;
                var t = document.createElement("x");
                return t.style.cssText = "pointer-events:auto", "auto" === t.style.pointerEvents
            }
        }(), Pt = function (t, e) {
            var n = R(t),
                o = parseInt(n.width) - parseInt(n.paddingLeft) - parseInt(n.paddingRight) - parseInt(n.borderLeftWidth) - parseInt(n.borderRightWidth),
                i = B(t, 0, e), r = B(t, 1, e), a = i && R(i), l = r && R(r),
                s = a && parseInt(a.marginLeft) + parseInt(a.marginRight) + X(i).width,
                t = l && parseInt(l.marginLeft) + parseInt(l.marginRight) + X(r).width;
            if ("flex" === n.display) return "column" === n.flexDirection || "column-reverse" === n.flexDirection ? "vertical" : "horizontal";
            if ("grid" === n.display) return n.gridTemplateColumns.split(" ").length <= 1 ? "vertical" : "horizontal";
            if (i && a.float && "none" !== a.float) {
                e = "left" === a.float ? "left" : "right";
                return !r || "both" !== l.clear && l.clear !== e ? "horizontal" : "vertical"
            }
            return i && ("block" === a.display || "flex" === a.display || "table" === a.display || "grid" === a.display || o <= s && "none" === n[At] || r && "none" === n[At] && o < s + t) ? "vertical" : "horizontal"
        }, kt = function (t) {
            function l(r, a) {
                return function (t, e, n, o) {
                    var i = t.options.group.name && e.options.group.name && t.options.group.name === e.options.group.name;
                    if (null == r && (a || i)) return !0;
                    if (null == r || !1 === r) return !1;
                    if (a && "clone" === r) return r;
                    if ("function" == typeof r) return l(r(t, e, n, o), a)(t, e, n, o);
                    e = (a ? t : e).options.group.name;
                    return !0 === r || "string" == typeof r && r === e || r.join && -1 < r.indexOf(e)
                }
            }

            var e = {}, n = t.group;
            n && "object" == o(n) || (n = {name: n}), e.name = n.name, e.checkPull = l(n.pull, !0), e.checkPut = l(n.put), e.revertClone = n.revertClone, t.group = e
        }, Rt = function () {
            !It && $ && R($, "display", "none")
        }, Xt = function () {
            !It && $ && R($, "display", "")
        };
    Ot && !c && document.addEventListener("click", function (t) {
        if (Et) return t.preventDefault(), t.stopPropagation && t.stopPropagation(), t.stopImmediatePropagation && t.stopImmediatePropagation(), Et = !1
    }, !0);

    function Yt(t) {
        if (V) {
            t = t.touches ? t.touches[0] : t;
            var e = (i = t.clientX, r = t.clientY, Dt.some(function (t) {
                var e = t[K].options.emptyInsertThreshold;
                if (e && !F(t)) {
                    var n = X(t), o = i >= n.left - e && i <= n.right + e, e = r >= n.top - e && r <= n.bottom + e;
                    return o && e ? a = t : void 0
                }
            }), a);
            if (e) {
                var n, o = {};
                for (n in t) t.hasOwnProperty(n) && (o[n] = t[n]);
                o.target = o.rootEl = e, o.preventDefault = void 0, o.stopPropagation = void 0, e[K]._onDragOver(o)
            }
        }
        var i, r, a
    }

    function Bt(t) {
        V && V.parentNode[K]._isOutsideThisEl(t.target)
    }

    function Ft(t, e) {
        if (!t || !t.nodeType || 1 !== t.nodeType) throw "Sortable: `el` must be an HTMLElement, not ".concat({}.toString.call(t));
        this.el = t, this.options = e = a({}, e), t[K] = this;
        var n, o, i = {
            group: null,
            sort: !0,
            disabled: !1,
            store: null,
            handle: null,
            draggable: /^[uo]l$/i.test(t.nodeName) ? ">li" : ">*",
            swapThreshold: 1,
            invertSwap: !1,
            invertedSwapThreshold: null,
            removeCloneOnHide: !0,
            direction: function () {
                return Pt(t, this.options)
            },
            ghostClass: "sortable-ghost",
            chosenClass: "sortable-chosen",
            dragClass: "sortable-drag",
            ignore: "a, img",
            filter: null,
            preventOnFilter: !0,
            animation: 0,
            easing: null,
            setData: function (t, e) {
                t.setData("Text", e.textContent)
            },
            dropBubble: !1,
            dragoverBubble: !1,
            dataIdAttr: "data-id",
            delay: 0,
            delayOnTouchOnly: !1,
            touchStartThreshold: (Number.parseInt ? Number : window).parseInt(window.devicePixelRatio, 10) || 1,
            forceFallback: !1,
            fallbackClass: "sortable-fallback",
            fallbackOnBody: !1,
            fallbackTolerance: 0,
            fallbackOffset: {x: 0, y: 0},
            supportPointer: !1 !== Ft.supportPointer && "PointerEvent" in window && !u,
            emptyInsertThreshold: 5
        };
        for (n in W.initializePlugins(this, t, i), i) n in e || (e[n] = i[n]);
        for (o in kt(e), this) "_" === o.charAt(0) && "function" == typeof this[o] && (this[o] = this[o].bind(this));
        this.nativeDraggable = !e.forceFallback && Nt, this.nativeDraggable && (this.options.touchStartThreshold = 1), e.supportPointer ? h(t, "pointerdown", this._onTapStart) : (h(t, "mousedown", this._onTapStart), h(t, "touchstart", this._onTapStart)), this.nativeDraggable && (h(t, "dragover", this), h(t, "dragenter", this)), Dt.push(this.el), e.store && e.store.get && this.sort(e.store.get(this) || []), a(this, x())
    }

    function jt(t, e, n, o, i, r, a, l) {
        var s, c, u = t[K], d = u.options.onMove;
        return !window.CustomEvent || y || w ? (s = document.createEvent("Event")).initEvent("move", !0, !0) : s = new CustomEvent("move", {
            bubbles: !0,
            cancelable: !0
        }), s.to = e, s.from = t, s.dragged = n, s.draggedRect = o, s.related = i || e, s.relatedRect = r || X(e), s.willInsertAfter = l, s.originalEvent = a, t.dispatchEvent(s), c = d ? d.call(u, s, a) : c
    }

    function Ht(t) {
        t.draggable = !1
    }

    function Lt() {
        Tt = !1
    }

    function Kt(t) {
        return setTimeout(t, 0)
    }

    function Wt(t) {
        return clearTimeout(t)
    }

    Ft.prototype = {
        constructor: Ft, _isOutsideThisEl: function (t) {
            this.el.contains(t) || t === this.el || (mt = null)
        }, _getDirection: function (t, e) {
            return "function" == typeof this.options.direction ? this.options.direction.call(this, t, e, V) : this.options.direction
        }, _onTapStart: function (e) {
            if (e.cancelable) {
                var n = this, o = this.el, t = this.options, i = t.preventOnFilter, r = e.type,
                    a = e.touches && e.touches[0] || e.pointerType && "touch" === e.pointerType && e,
                    l = (a || e).target,
                    s = e.target.shadowRoot && (e.path && e.path[0] || e.composedPath && e.composedPath()[0]) || l,
                    c = t.filter;
                if (!function (t) {
                    xt.length = 0;
                    var e = t.getElementsByTagName("input"), n = e.length;
                    for (; n--;) {
                        var o = e[n];
                        o.checked && xt.push(o)
                    }
                }(o), !V && !(/mousedown|pointerdown/.test(r) && 0 !== e.button || t.disabled) && !s.isContentEditable && (this.nativeDraggable || !u || !l || "SELECT" !== l.tagName.toUpperCase()) && !((l = P(l, t.draggable, o, !1)) && l.animated || tt === l)) {
                    if (ot = j(l), rt = j(l, t.draggable), "function" == typeof c) {
                        if (c.call(this, e, l, this)) return q({
                            sortable: n,
                            rootEl: s,
                            name: "filter",
                            targetEl: l,
                            toEl: o,
                            fromEl: o
                        }), G("filter", n, {evt: e}), void (i && e.cancelable && e.preventDefault())
                    } else if (c = c && c.split(",").some(function (t) {
                        if (t = P(s, t.trim(), o, !1)) return q({
                            sortable: n,
                            rootEl: t,
                            name: "filter",
                            targetEl: l,
                            fromEl: o,
                            toEl: o
                        }), G("filter", n, {evt: e}), !0
                    })) return void (i && e.cancelable && e.preventDefault());
                    t.handle && !P(s, t.handle, o, !1) || this._prepareDragStart(e, a, l)
                }
            }
        }, _prepareDragStart: function (t, e, n) {
            var o, i = this, r = i.el, a = i.options, l = r.ownerDocument;
            n && !V && n.parentNode === r && (o = X(n), Q = r, Z = (V = n).parentNode, J = V.nextSibling, tt = n, lt = a.group, ct = {
                target: Ft.dragged = V,
                clientX: (e || t).clientX,
                clientY: (e || t).clientY
            }, ft = ct.clientX - o.left, pt = ct.clientY - o.top, this._lastX = (e || t).clientX, this._lastY = (e || t).clientY, V.style["will-change"] = "all", o = function () {
                G("delayEnded", i, {evt: t}), Ft.eventCanceled ? i._onDrop() : (i._disableDelayedDragEvents(), !s && i.nativeDraggable && (V.draggable = !0), i._triggerDragStart(t, e), q({
                    sortable: i,
                    name: "choose",
                    originalEvent: t
                }), k(V, a.chosenClass, !0))
            }, a.ignore.split(",").forEach(function (t) {
                b(V, t.trim(), Ht)
            }), h(l, "dragover", Yt), h(l, "mousemove", Yt), h(l, "touchmove", Yt), h(l, "mouseup", i._onDrop), h(l, "touchend", i._onDrop), h(l, "touchcancel", i._onDrop), s && this.nativeDraggable && (this.options.touchStartThreshold = 4, V.draggable = !0), G("delayStart", this, {evt: t}), !a.delay || a.delayOnTouchOnly && !e || this.nativeDraggable && (w || y) ? o() : Ft.eventCanceled ? this._onDrop() : (h(l, "mouseup", i._disableDelayedDrag), h(l, "touchend", i._disableDelayedDrag), h(l, "touchcancel", i._disableDelayedDrag), h(l, "mousemove", i._delayedDragTouchMoveHandler), h(l, "touchmove", i._delayedDragTouchMoveHandler), a.supportPointer && h(l, "pointermove", i._delayedDragTouchMoveHandler), i._dragStartTimer = setTimeout(o, a.delay)))
        }, _delayedDragTouchMoveHandler: function (t) {
            t = t.touches ? t.touches[0] : t;
            Math.max(Math.abs(t.clientX - this._lastX), Math.abs(t.clientY - this._lastY)) >= Math.floor(this.options.touchStartThreshold / (this.nativeDraggable && window.devicePixelRatio || 1)) && this._disableDelayedDrag()
        }, _disableDelayedDrag: function () {
            V && Ht(V), clearTimeout(this._dragStartTimer), this._disableDelayedDragEvents()
        }, _disableDelayedDragEvents: function () {
            var t = this.el.ownerDocument;
            f(t, "mouseup", this._disableDelayedDrag), f(t, "touchend", this._disableDelayedDrag), f(t, "touchcancel", this._disableDelayedDrag), f(t, "mousemove", this._delayedDragTouchMoveHandler), f(t, "touchmove", this._delayedDragTouchMoveHandler), f(t, "pointermove", this._delayedDragTouchMoveHandler)
        }, _triggerDragStart: function (t, e) {
            e = e || "touch" == t.pointerType && t, !this.nativeDraggable || e ? this.options.supportPointer ? h(document, "pointermove", this._onTouchMove) : h(document, e ? "touchmove" : "mousemove", this._onTouchMove) : (h(V, "dragend", this), h(Q, "dragstart", this._onDragStart));
            try {
                document.selection ? Kt(function () {
                    document.selection.empty()
                }) : window.getSelection().removeAllRanges()
            } catch (t) {
            }
        }, _dragStarted: function (t, e) {
            var n;
            wt = !1, Q && V ? (G("dragStarted", this, {evt: e}), this.nativeDraggable && h(document, "dragover", Bt), n = this.options, t || k(V, n.dragClass, !1), k(V, n.ghostClass, !0), Ft.active = this, t && this._appendGhost(), q({
                sortable: this,
                name: "start",
                originalEvent: e
            })) : this._nulling()
        }, _emulateDragOver: function () {
            if (ut) {
                this._lastX = ut.clientX, this._lastY = ut.clientY, Rt();
                for (var t = document.elementFromPoint(ut.clientX, ut.clientY), e = t; t && t.shadowRoot && (t = t.shadowRoot.elementFromPoint(ut.clientX, ut.clientY)) !== e;) e = t;
                if (V.parentNode[K]._isOutsideThisEl(t), e) do {
                    if (e[K]) if (e[K]._onDragOver({
                        clientX: ut.clientX,
                        clientY: ut.clientY,
                        target: t,
                        rootEl: e
                    }) && !this.options.dragoverBubble) break
                } while (e = (t = e).parentNode);
                Xt()
            }
        }, _onTouchMove: function (t) {
            if (ct) {
                var e = this.options, n = e.fallbackTolerance, o = e.fallbackOffset, i = t.touches ? t.touches[0] : t,
                    r = $ && v($, !0), a = $ && r && r.a, l = $ && r && r.d, e = Mt && yt && E(yt),
                    a = (i.clientX - ct.clientX + o.x) / (a || 1) + (e ? e[0] - Ct[0] : 0) / (a || 1),
                    l = (i.clientY - ct.clientY + o.y) / (l || 1) + (e ? e[1] - Ct[1] : 0) / (l || 1);
                if (!Ft.active && !wt) {
                    if (n && Math.max(Math.abs(i.clientX - this._lastX), Math.abs(i.clientY - this._lastY)) < n) return;
                    this._onDragStart(t, !0)
                }
                $ && (r ? (r.e += a - (dt || 0), r.f += l - (ht || 0)) : r = {
                    a: 1,
                    b: 0,
                    c: 0,
                    d: 1,
                    e: a,
                    f: l
                }, r = "matrix(".concat(r.a, ",").concat(r.b, ",").concat(r.c, ",").concat(r.d, ",").concat(r.e, ",").concat(r.f, ")"), R($, "webkitTransform", r), R($, "mozTransform", r), R($, "msTransform", r), R($, "transform", r), dt = a, ht = l, ut = i), t.cancelable && t.preventDefault()
            }
        }, _appendGhost: function () {
            if (!$) {
                var t = this.options.fallbackOnBody ? document.body : Q, e = X(V, !0, Mt, !0, t), n = this.options;
                if (Mt) {
                    for (yt = t; "static" === R(yt, "position") && "none" === R(yt, "transform") && yt !== document;) yt = yt.parentNode;
                    yt !== document.body && yt !== document.documentElement ? (yt === document && (yt = O()), e.top += yt.scrollTop, e.left += yt.scrollLeft) : yt = O(), Ct = E(yt)
                }
                k($ = V.cloneNode(!0), n.ghostClass, !1), k($, n.fallbackClass, !0), k($, n.dragClass, !0), R($, "transition", ""), R($, "transform", ""), R($, "box-sizing", "border-box"), R($, "margin", 0), R($, "top", e.top), R($, "left", e.left), R($, "width", e.width), R($, "height", e.height), R($, "opacity", "0.8"), R($, "position", Mt ? "absolute" : "fixed"), R($, "zIndex", "100000"), R($, "pointerEvents", "none"), Ft.ghost = $, t.appendChild($), R($, "transform-origin", ft / parseInt($.style.width) * 100 + "% " + pt / parseInt($.style.height) * 100 + "%")
            }
        }, _onDragStart: function (t, e) {
            var n = this, o = t.dataTransfer, i = n.options;
            G("dragStart", this, {evt: t}), Ft.eventCanceled ? this._onDrop() : (G("setupClone", this), Ft.eventCanceled || ((et = _(V)).removeAttribute("id"), et.draggable = !1, et.style["will-change"] = "", this._hideClone(), k(et, this.options.chosenClass, !1), Ft.clone = et), n.cloneId = Kt(function () {
                G("clone", n), Ft.eventCanceled || (n.options.removeCloneOnHide || Q.insertBefore(et, V), n._hideClone(), q({
                    sortable: n,
                    name: "clone"
                }))
            }), e || k(V, i.dragClass, !0), e ? (Et = !0, n._loopId = setInterval(n._emulateDragOver, 50)) : (f(document, "mouseup", n._onDrop), f(document, "touchend", n._onDrop), f(document, "touchcancel", n._onDrop), o && (o.effectAllowed = "move", i.setData && i.setData.call(n, o, V)), h(document, "drop", n), R(V, "transform", "translateZ(0)")), wt = !0, n._dragStartId = Kt(n._dragStarted.bind(n, e, t)), h(document, "selectstart", n), gt = !0, u && R(document.body, "user-select", "none"))
        }, _onDragOver: function (n) {
            var o, i, r, t, e, a = this.el, l = n.target, s = this.options, c = s.group, u = Ft.active, d = lt === c,
                h = s.sort, f = st || u, p = this, g = !1;
            if (!Tt) {
                if (void 0 !== n.preventDefault && n.cancelable && n.preventDefault(), l = P(l, s.draggable, a, !0), O("dragOver"), Ft.eventCanceled) return g;
                if (V.contains(n.target) || l.animated && l.animatingX && l.animatingY || p._ignoreWhileAnimating === l) return A(!1);
                if (Et = !1, u && !s.disabled && (d ? h || (i = Z !== Q) : st === this || (this.lastPutMode = lt.checkPull(this, u, V, n)) && c.checkPut(this, u, V, n))) {
                    if (r = "vertical" === this._getDirection(n, l), o = X(V), O("dragOverValid"), Ft.eventCanceled) return g;
                    if (i) return Z = Q, M(), this._hideClone(), O("revert"), Ft.eventCanceled || (J ? Q.insertBefore(V, J) : Q.appendChild(V)), A(!0);
                    var m = F(a, s.draggable);
                    if (m && (S = n, c = r, x = X(F((D = this).el, D.options.draggable)), D = L(D.el, D.options, $), !(c ? S.clientX > D.right + 10 || S.clientY > x.bottom && S.clientX > x.left : S.clientY > D.bottom + 10 || S.clientX > x.right && S.clientY > x.top) || m.animated)) {
                        if (m && (t = n, e = r, C = X(B((_ = this).el, 0, _.options, !0)), _ = L(_.el, _.options, $), e ? t.clientX < _.left - 10 || t.clientY < C.top && t.clientX < C.right : t.clientY < _.top - 10 || t.clientY < C.bottom && t.clientX < C.left)) {
                            var v = B(a, 0, s, !0);
                            if (v === V) return A(!1);
                            if (E = X(l = v), !1 !== jt(Q, a, V, o, l, E, n, !1)) return M(), a.insertBefore(V, v), Z = a, N(), A(!0)
                        } else if (l.parentNode === a) {
                            var b, y, w, E = X(l), D = V.parentNode !== a,
                                S = (S = V.animated && V.toRect || o, x = l.animated && l.toRect || E, _ = (e = r) ? S.left : S.top, t = e ? S.right : S.bottom, C = e ? S.width : S.height, v = e ? x.left : x.top, S = e ? x.right : x.bottom, x = e ? x.width : x.height, !(_ === v || t === S || _ + C / 2 === v + x / 2)),
                                _ = r ? "top" : "left", C = Y(l, "top", "top") || Y(V, "top", "top"),
                                v = C ? C.scrollTop : void 0;
                            if (mt !== l && (y = E[_], St = !1, _t = !S && s.invertSwap || D), 0 !== (b = function (t, e, n, o, i, r, a, l) {
                                var s = o ? t.clientY : t.clientX, c = o ? n.height : n.width, t = o ? n.top : n.left,
                                    o = o ? n.bottom : n.right, n = !1;
                                if (!a) if (l && bt < c * i) {
                                    if (St = !St && (1 === vt ? t + c * r / 2 < s : s < o - c * r / 2) ? !0 : St) n = !0; else if (1 === vt ? s < t + bt : o - bt < s) return -vt
                                } else if (t + c * (1 - i) / 2 < s && s < o - c * (1 - i) / 2) return function (t) {
                                    return j(V) < j(t) ? 1 : -1
                                }(e);
                                if ((n = n || a) && (s < t + c * r / 2 || o - c * r / 2 < s)) return t + c / 2 < s ? 1 : -1;
                                return 0
                            }(n, l, E, r, S ? 1 : s.swapThreshold, null == s.invertedSwapThreshold ? s.swapThreshold : s.invertedSwapThreshold, _t, mt === l))) for (var T = j(V); (w = Z.children[T -= b]) && ("none" === R(w, "display") || w === $);) ;
                            if (0 === b || w === l) return A(!1);
                            vt = b;
                            var x = (mt = l).nextElementSibling, D = !1, S = jt(Q, a, V, o, l, E, n, D = 1 === b);
                            if (!1 !== S) return 1 !== S && -1 !== S || (D = 1 === S), Tt = !0, setTimeout(Lt, 30), M(), D && !x ? a.appendChild(V) : l.parentNode.insertBefore(V, D ? x : l), C && H(C, 0, v - C.scrollTop), Z = V.parentNode, void 0 === y || _t || (bt = Math.abs(y - X(l)[_])), N(), A(!0)
                        }
                    } else {
                        if (m === V) return A(!1);
                        if ((l = m && a === n.target ? m : l) && (E = X(l)), !1 !== jt(Q, a, V, o, l, E, n, !!l)) return M(), m && m.nextSibling ? a.insertBefore(V, m.nextSibling) : a.appendChild(V), Z = a, N(), A(!0)
                    }
                    if (a.contains(V)) return A(!1)
                }
                return !1
            }

            function O(t, e) {
                G(t, p, I({
                    evt: n,
                    isOwner: d,
                    axis: r ? "vertical" : "horizontal",
                    revert: i,
                    dragRect: o,
                    targetRect: E,
                    canSort: h,
                    fromSortable: f,
                    target: l,
                    completed: A,
                    onMove: function (t, e) {
                        return jt(Q, a, V, o, t, X(t), n, e)
                    },
                    changed: N
                }, e))
            }

            function M() {
                O("dragOverAnimationCapture"), p.captureAnimationState(), p !== f && f.captureAnimationState()
            }

            function A(t) {
                return O("dragOverCompleted", {insertion: t}), t && (d ? u._hideClone() : u._showClone(p), p !== f && (k(V, (st || u).options.ghostClass, !1), k(V, s.ghostClass, !0)), st !== p && p !== Ft.active ? st = p : p === Ft.active && st && (st = null), f === p && (p._ignoreWhileAnimating = l), p.animateAll(function () {
                    O("dragOverAnimationComplete"), p._ignoreWhileAnimating = null
                }), p !== f && (f.animateAll(), f._ignoreWhileAnimating = null)), (l === V && !V.animated || l === a && !l.animated) && (mt = null), s.dragoverBubble || n.rootEl || l === document || (V.parentNode[K]._isOutsideThisEl(n.target), t || Yt(n)), !s.dragoverBubble && n.stopPropagation && n.stopPropagation(), g = !0
            }

            function N() {
                it = j(V), at = j(V, s.draggable), q({
                    sortable: p,
                    name: "change",
                    toEl: a,
                    newIndex: it,
                    newDraggableIndex: at,
                    originalEvent: n
                })
            }
        }, _ignoreWhileAnimating: null, _offMoveEvents: function () {
            f(document, "mousemove", this._onTouchMove), f(document, "touchmove", this._onTouchMove), f(document, "pointermove", this._onTouchMove), f(document, "dragover", Yt), f(document, "mousemove", Yt), f(document, "touchmove", Yt)
        }, _offUpEvents: function () {
            var t = this.el.ownerDocument;
            f(t, "mouseup", this._onDrop), f(t, "touchend", this._onDrop), f(t, "pointerup", this._onDrop), f(t, "touchcancel", this._onDrop), f(document, "selectstart", this)
        }, _onDrop: function (t) {
            var e = this.el, n = this.options;
            it = j(V), at = j(V, n.draggable), G("drop", this, {evt: t}), Z = V && V.parentNode, it = j(V), at = j(V, n.draggable), Ft.eventCanceled || (St = _t = wt = !1, clearInterval(this._loopId), clearTimeout(this._dragStartTimer), Wt(this.cloneId), Wt(this._dragStartId), this.nativeDraggable && (f(document, "drop", this), f(e, "dragstart", this._onDragStart)), this._offMoveEvents(), this._offUpEvents(), u && R(document.body, "user-select", ""), R(V, "transform", ""), t && (gt && (t.cancelable && t.preventDefault(), n.dropBubble || t.stopPropagation()), $ && $.parentNode && $.parentNode.removeChild($), (Q === Z || st && "clone" !== st.lastPutMode) && et && et.parentNode && et.parentNode.removeChild(et), V && (this.nativeDraggable && f(V, "dragend", this), Ht(V), V.style["will-change"] = "", gt && !wt && k(V, (st || this).options.ghostClass, !1), k(V, this.options.chosenClass, !1), q({
                sortable: this,
                name: "unchoose",
                toEl: Z,
                newIndex: null,
                newDraggableIndex: null,
                originalEvent: t
            }), Q !== Z ? (0 <= it && (q({
                rootEl: Z,
                name: "add",
                toEl: Z,
                fromEl: Q,
                originalEvent: t
            }), q({sortable: this, name: "remove", toEl: Z, originalEvent: t}), q({
                rootEl: Z,
                name: "sort",
                toEl: Z,
                fromEl: Q,
                originalEvent: t
            }), q({
                sortable: this,
                name: "sort",
                toEl: Z,
                originalEvent: t
            })), st && st.save()) : it !== ot && 0 <= it && (q({
                sortable: this,
                name: "update",
                toEl: Z,
                originalEvent: t
            }), q({
                sortable: this,
                name: "sort",
                toEl: Z,
                originalEvent: t
            })), Ft.active && (null != it && -1 !== it || (it = ot, at = rt), q({
                sortable: this,
                name: "end",
                toEl: Z,
                originalEvent: t
            }), this.save())))), this._nulling()
        }, _nulling: function () {
            G("nulling", this), Q = V = Z = $ = J = et = tt = nt = ct = ut = gt = it = at = ot = rt = mt = vt = st = lt = Ft.dragged = Ft.ghost = Ft.clone = Ft.active = null, xt.forEach(function (t) {
                t.checked = !0
            }), xt.length = dt = ht = 0
        }, handleEvent: function (t) {
            switch (t.type) {
                case"drop":
                case"dragend":
                    this._onDrop(t);
                    break;
                case"dragenter":
                case"dragover":
                    V && (this._onDragOver(t), function (t) {
                        t.dataTransfer && (t.dataTransfer.dropEffect = "move");
                        t.cancelable && t.preventDefault()
                    }(t));
                    break;
                case"selectstart":
                    t.preventDefault()
            }
        }, toArray: function () {
            for (var t, e = [], n = this.el.children, o = 0, i = n.length, r = this.options; o < i; o++) P(t = n[o], r.draggable, this.el, !1) && e.push(t.getAttribute(r.dataIdAttr) || function (t) {
                var e = t.tagName + t.className + t.src + t.href + t.textContent, n = e.length, o = 0;
                for (; n--;) o += e.charCodeAt(n);
                return o.toString(36)
            }(t));
            return e
        }, sort: function (t, e) {
            var n = {}, o = this.el;
            this.toArray().forEach(function (t, e) {
                e = o.children[e];
                P(e, this.options.draggable, o, !1) && (n[t] = e)
            }, this), e && this.captureAnimationState(), t.forEach(function (t) {
                n[t] && (o.removeChild(n[t]), o.appendChild(n[t]))
            }), e && this.animateAll()
        }, save: function () {
            var t = this.options.store;
            t && t.set && t.set(this)
        }, closest: function (t, e) {
            return P(t, e || this.options.draggable, this.el, !1)
        }, option: function (t, e) {
            var n = this.options;
            if (void 0 === e) return n[t];
            var o = W.modifyOption(this, t, e);
            n[t] = void 0 !== o ? o : e, "group" === t && kt(n)
        }, destroy: function () {
            G("destroy", this);
            var t = this.el;
            t[K] = null, f(t, "mousedown", this._onTapStart), f(t, "touchstart", this._onTapStart), f(t, "pointerdown", this._onTapStart), this.nativeDraggable && (f(t, "dragover", this), f(t, "dragenter", this)), Array.prototype.forEach.call(t.querySelectorAll("[draggable]"), function (t) {
                t.removeAttribute("draggable")
            }), this._onDrop(), this._disableDelayedDragEvents(), Dt.splice(Dt.indexOf(this.el), 1), this.el = t = null
        }, _hideClone: function () {
            nt || (G("hideClone", this), Ft.eventCanceled || (R(et, "display", "none"), this.options.removeCloneOnHide && et.parentNode && et.parentNode.removeChild(et), nt = !0))
        }, _showClone: function (t) {
            "clone" === t.lastPutMode ? nt && (G("showClone", this), Ft.eventCanceled || (V.parentNode != Q || this.options.group.revertClone ? J ? Q.insertBefore(et, J) : Q.appendChild(et) : Q.insertBefore(et, V), this.options.group.revertClone && this.animate(V, et), R(et, "display", ""), nt = !1)) : this._hideClone()
        }
    }, Ot && h(document, "touchmove", function (t) {
        (Ft.active || wt) && t.cancelable && t.preventDefault()
    }), Ft.utils = {
        on: h,
        off: f,
        css: R,
        find: b,
        is: function (t, e) {
            return !!P(t, e, t, !1)
        },
        extend: function (t, e) {
            if (t && e) for (var n in e) e.hasOwnProperty(n) && (t[n] = e[n]);
            return t
        },
        throttle: S,
        closest: P,
        toggleClass: k,
        clone: _,
        index: j,
        nextTick: Kt,
        cancelNextTick: Wt,
        detectDirection: Pt,
        getChild: B
    }, Ft.get = function (t) {
        return t[K]
    }, Ft.mount = function () {
        for (var t = arguments.length, e = new Array(t), n = 0; n < t; n++) e[n] = arguments[n];
        (e = e[0].constructor === Array ? e[0] : e).forEach(function (t) {
            if (!t.prototype || !t.prototype.constructor) throw "Sortable: Mounted plugin must be a constructor function, not ".concat({}.toString.call(t));
            t.utils && (Ft.utils = I(I({}, Ft.utils), t.utils)), W.mount(t)
        })
    }, Ft.create = function (t, e) {
        return new Ft(t, e)
    };
    var zt, Gt, Ut, qt, Vt, Zt, $t = [], Qt = !(Ft.version = "1.15.2");

    function Jt() {
        $t.forEach(function (t) {
            clearInterval(t.pid)
        }), $t = []
    }

    function te() {
        clearInterval(Zt)
    }

    var ee, ne = S(function (n, t, e, o) {
        if (t.scroll) {
            var i, r = (n.touches ? n.touches[0] : n).clientX, a = (n.touches ? n.touches[0] : n).clientY,
                l = t.scrollSensitivity, s = t.scrollSpeed, c = O(), u = !1;
            Gt !== e && (Gt = e, Jt(), zt = t.scroll, i = t.scrollFn, !0 === zt && (zt = M(e, !0)));
            var d = 0, h = zt;
            do {
                var f = h, p = X(f), g = p.top, m = p.bottom, v = p.left, b = p.right, y = p.width, w = p.height,
                    E = void 0, D = void 0, S = f.scrollWidth, _ = f.scrollHeight, C = R(f), T = f.scrollLeft,
                    p = f.scrollTop,
                    D = f === c ? (E = y < S && ("auto" === C.overflowX || "scroll" === C.overflowX || "visible" === C.overflowX), w < _ && ("auto" === C.overflowY || "scroll" === C.overflowY || "visible" === C.overflowY)) : (E = y < S && ("auto" === C.overflowX || "scroll" === C.overflowX), w < _ && ("auto" === C.overflowY || "scroll" === C.overflowY)),
                    T = E && (Math.abs(b - r) <= l && T + y < S) - (Math.abs(v - r) <= l && !!T),
                    p = D && (Math.abs(m - a) <= l && p + w < _) - (Math.abs(g - a) <= l && !!p);
                if (!$t[d]) for (var x = 0; x <= d; x++) $t[x] || ($t[x] = {});
                $t[d].vx == T && $t[d].vy == p && $t[d].el === f || ($t[d].el = f, $t[d].vx = T, $t[d].vy = p, clearInterval($t[d].pid), 0 == T && 0 == p || (u = !0, $t[d].pid = setInterval(function () {
                    o && 0 === this.layer && Ft.active._onTouchMove(Vt);
                    var t = $t[this.layer].vy ? $t[this.layer].vy * s : 0,
                        e = $t[this.layer].vx ? $t[this.layer].vx * s : 0;
                    "function" == typeof i && "continue" !== i.call(Ft.dragged.parentNode[K], e, t, n, Vt, $t[this.layer].el) || H($t[this.layer].el, e, t)
                }.bind({layer: d}), 24))), d++
            } while (t.bubbleScroll && h !== c && (h = M(h, !1)));
            Qt = u
        }
    }, 30), c = function (t) {
        var e = t.originalEvent, n = t.putSortable, o = t.dragEl, i = t.activeSortable, r = t.dispatchSortableEvent,
            a = t.hideGhostForTarget, t = t.unhideGhostForTarget;
        e && (i = n || i, a(), e = e.changedTouches && e.changedTouches.length ? e.changedTouches[0] : e, e = document.elementFromPoint(e.clientX, e.clientY), t(), i && !i.el.contains(e) && (r("spill"), this.onSpill({
            dragEl: o,
            putSortable: n
        })))
    };

    function oe() {
    }

    function ie() {
    }

    oe.prototype = {
        startIndex: null, dragStart: function (t) {
            t = t.oldDraggableIndex;
            this.startIndex = t
        }, onSpill: function (t) {
            var e = t.dragEl, n = t.putSortable;
            this.sortable.captureAnimationState(), n && n.captureAnimationState();
            t = B(this.sortable.el, this.startIndex, this.options);
            t ? this.sortable.el.insertBefore(e, t) : this.sortable.el.appendChild(e), this.sortable.animateAll(), n && n.animateAll()
        }, drop: c
    }, a(oe, {pluginName: "revertOnSpill"}), ie.prototype = {
        onSpill: function (t) {
            var e = t.dragEl, t = t.putSortable || this.sortable;
            t.captureAnimationState(), e.parentNode && e.parentNode.removeChild(e), t.animateAll()
        }, drop: c
    }, a(ie, {pluginName: "removeOnSpill"});
    var re, ae, le, se, ce, ue = [], de = [], he = !1, fe = !1, pe = !1;

    function ge(n, o) {
        de.forEach(function (t, e) {
            e = o.children[t.sortableIndex + (n ? Number(e) : 0)];
            e ? o.insertBefore(t, e) : o.appendChild(t)
        })
    }

    function me() {
        ue.forEach(function (t) {
            t !== le && t.parentNode && t.parentNode.removeChild(t)
        })
    }

    return Ft.mount(new function () {
        function t() {
            for (var t in this.defaults = {
                scroll: !0,
                forceAutoScrollFallback: !1,
                scrollSensitivity: 30,
                scrollSpeed: 10,
                bubbleScroll: !0
            }, this) "_" === t.charAt(0) && "function" == typeof this[t] && (this[t] = this[t].bind(this))
        }

        return t.prototype = {
            dragStarted: function (t) {
                t = t.originalEvent;
                this.sortable.nativeDraggable ? h(document, "dragover", this._handleAutoScroll) : this.options.supportPointer ? h(document, "pointermove", this._handleFallbackAutoScroll) : t.touches ? h(document, "touchmove", this._handleFallbackAutoScroll) : h(document, "mousemove", this._handleFallbackAutoScroll)
            }, dragOverCompleted: function (t) {
                t = t.originalEvent;
                this.options.dragOverBubble || t.rootEl || this._handleAutoScroll(t)
            }, drop: function () {
                this.sortable.nativeDraggable ? f(document, "dragover", this._handleAutoScroll) : (f(document, "pointermove", this._handleFallbackAutoScroll), f(document, "touchmove", this._handleFallbackAutoScroll), f(document, "mousemove", this._handleFallbackAutoScroll)), te(), Jt(), clearTimeout(g), g = void 0
            }, nulling: function () {
                Vt = Gt = zt = Qt = Zt = Ut = qt = null, $t.length = 0
            }, _handleFallbackAutoScroll: function (t) {
                this._handleAutoScroll(t, !0)
            }, _handleAutoScroll: function (e, n) {
                var o, i = this, r = (e.touches ? e.touches[0] : e).clientX, a = (e.touches ? e.touches[0] : e).clientY,
                    t = document.elementFromPoint(r, a);
                Vt = e, n || this.options.forceAutoScrollFallback || w || y || u ? (ne(e, this.options, t, n), o = M(t, !0), !Qt || Zt && r === Ut && a === qt || (Zt && te(), Zt = setInterval(function () {
                    var t = M(document.elementFromPoint(r, a), !0);
                    t !== o && (o = t, Jt()), ne(e, i.options, t, n)
                }, 10), Ut = r, qt = a)) : this.options.bubbleScroll && M(t, !0) !== O() ? ne(e, this.options, M(t, !1), !1) : Jt()
            }
        }, a(t, {pluginName: "scroll", initializeByDefault: !0})
    }), Ft.mount(ie, oe), Ft.mount(new function () {
        function t() {
            this.defaults = {swapClass: "sortable-swap-highlight"}
        }

        return t.prototype = {
            dragStart: function (t) {
                t = t.dragEl;
                ee = t
            }, dragOverValid: function (t) {
                var e = t.completed, n = t.target, o = t.onMove, i = t.activeSortable, r = t.changed, a = t.cancel;
                i.options.swap && (t = this.sortable.el, i = this.options, n && n !== t && (t = ee, ee = !1 !== o(n) ? (k(n, i.swapClass, !0), n) : null, t && t !== ee && k(t, i.swapClass, !1)), r(), e(!0), a())
            }, drop: function (t) {
                var e, n, o = t.activeSortable, i = t.putSortable, r = t.dragEl, a = i || this.sortable,
                    l = this.options;
                ee && k(ee, l.swapClass, !1), ee && (l.swap || i && i.options.swap) && r !== ee && (a.captureAnimationState(), a !== o && o.captureAnimationState(), n = ee, t = (e = r).parentNode, l = n.parentNode, t && l && !t.isEqualNode(n) && !l.isEqualNode(e) && (i = j(e), r = j(n), t.isEqualNode(l) && i < r && r++, t.insertBefore(n, t.children[i]), l.insertBefore(e, l.children[r])), a.animateAll(), a !== o && o.animateAll())
            }, nulling: function () {
                ee = null
            }
        }, a(t, {
            pluginName: "swap", eventProperties: function () {
                return {swapItem: ee}
            }
        })
    }), Ft.mount(new function () {
        function t(o) {
            for (var t in this) "_" === t.charAt(0) && "function" == typeof this[t] && (this[t] = this[t].bind(this));
            o.options.avoidImplicitDeselect || (o.options.supportPointer ? h(document, "pointerup", this._deselectMultiDrag) : (h(document, "mouseup", this._deselectMultiDrag), h(document, "touchend", this._deselectMultiDrag))), h(document, "keydown", this._checkKeyDown), h(document, "keyup", this._checkKeyUp), this.defaults = {
                selectedClass: "sortable-selected",
                multiDragKey: null,
                avoidImplicitDeselect: !1,
                setData: function (t, e) {
                    var n = "";
                    ue.length && ae === o ? ue.forEach(function (t, e) {
                        n += (e ? ", " : "") + t.textContent
                    }) : n = e.textContent, t.setData("Text", n)
                }
            }
        }

        return t.prototype = {
            multiDragKeyDown: !1, isMultiDrag: !1, delayStartGlobal: function (t) {
                t = t.dragEl;
                le = t
            }, delayEnded: function () {
                this.isMultiDrag = ~ue.indexOf(le)
            }, setupClone: function (t) {
                var e = t.sortable, t = t.cancel;
                if (this.isMultiDrag) {
                    for (var n = 0; n < ue.length; n++) de.push(_(ue[n])), de[n].sortableIndex = ue[n].sortableIndex, de[n].draggable = !1, de[n].style["will-change"] = "", k(de[n], this.options.selectedClass, !1), ue[n] === le && k(de[n], this.options.chosenClass, !1);
                    e._hideClone(), t()
                }
            }, clone: function (t) {
                var e = t.sortable, n = t.rootEl, o = t.dispatchSortableEvent, t = t.cancel;
                this.isMultiDrag && (this.options.removeCloneOnHide || ue.length && ae === e && (ge(!0, n), o("clone"), t()))
            }, showClone: function (t) {
                var e = t.cloneNowShown, n = t.rootEl, t = t.cancel;
                this.isMultiDrag && (ge(!1, n), de.forEach(function (t) {
                    R(t, "display", "")
                }), e(), ce = !1, t())
            }, hideClone: function (t) {
                var e = this, n = (t.sortable, t.cloneNowHidden), t = t.cancel;
                this.isMultiDrag && (de.forEach(function (t) {
                    R(t, "display", "none"), e.options.removeCloneOnHide && t.parentNode && t.parentNode.removeChild(t)
                }), n(), ce = !0, t())
            }, dragStartGlobal: function (t) {
                t.sortable;
                !this.isMultiDrag && ae && ae.multiDrag._deselectMultiDrag(), ue.forEach(function (t) {
                    t.sortableIndex = j(t)
                }), ue = ue.sort(function (t, e) {
                    return t.sortableIndex - e.sortableIndex
                }), pe = !0
            }, dragStarted: function (t) {
                var e, n = this, t = t.sortable;
                this.isMultiDrag && (this.options.sort && (t.captureAnimationState(), this.options.animation && (ue.forEach(function (t) {
                    t !== le && R(t, "position", "absolute")
                }), e = X(le, !1, !0, !0), ue.forEach(function (t) {
                    t !== le && C(t, e)
                }), he = fe = !0)), t.animateAll(function () {
                    he = fe = !1, n.options.animation && ue.forEach(function (t) {
                        T(t)
                    }), n.options.sort && me()
                }))
            }, dragOver: function (t) {
                var e = t.target, n = t.completed, t = t.cancel;
                fe && ~ue.indexOf(e) && (n(!1), t())
            }, revert: function (t) {
                var n, o, e = t.fromSortable, i = t.rootEl, r = t.sortable, a = t.dragRect;
                1 < ue.length && (ue.forEach(function (t) {
                    r.addAnimationState({
                        target: t,
                        rect: fe ? X(t) : a
                    }), T(t), t.fromRect = a, e.removeAnimationState(t)
                }), fe = !1, n = !this.options.removeCloneOnHide, o = i, ue.forEach(function (t, e) {
                    e = o.children[t.sortableIndex + (n ? Number(e) : 0)];
                    e ? o.insertBefore(t, e) : o.appendChild(t)
                }))
            }, dragOverCompleted: function (t) {
                var e, n = t.sortable, o = t.isOwner, i = t.insertion, r = t.activeSortable, a = t.parentEl,
                    l = t.putSortable, t = this.options;
                i && (o && r._hideClone(), he = !1, t.animation && 1 < ue.length && (fe || !o && !r.options.sort && !l) && (e = X(le, !1, !0, !0), ue.forEach(function (t) {
                    t !== le && (C(t, e), a.appendChild(t))
                }), fe = !0), o || (fe || me(), 1 < ue.length ? (o = ce, r._showClone(n), r.options.animation && !ce && o && de.forEach(function (t) {
                    r.addAnimationState({target: t, rect: se}), t.fromRect = se, t.thisAnimationDuration = null
                })) : r._showClone(n)))
            }, dragOverAnimationCapture: function (t) {
                var e = t.dragRect, n = t.isOwner, t = t.activeSortable;
                ue.forEach(function (t) {
                    t.thisAnimationDuration = null
                }), t.options.animation && !n && t.multiDrag.isMultiDrag && (se = a({}, e), e = v(le, !0), se.top -= e.f, se.left -= e.e)
            }, dragOverAnimationComplete: function () {
                fe && (fe = !1, me())
            }, drop: function (t) {
                var e = t.originalEvent, n = t.rootEl, o = t.parentEl, i = t.sortable, r = t.dispatchSortableEvent,
                    a = t.oldIndex, l = t.putSortable, s = l || this.sortable;
                if (e) {
                    var c, u, d, h = this.options, f = o.children;
                    if (!pe) if (h.multiDragKey && !this.multiDragKeyDown && this._deselectMultiDrag(), k(le, h.selectedClass, !~ue.indexOf(le)), ~ue.indexOf(le)) ue.splice(ue.indexOf(le), 1), re = null, z({
                        sortable: i,
                        rootEl: n,
                        name: "deselect",
                        targetEl: le,
                        originalEvent: e
                    }); else {
                        if (ue.push(le), z({
                            sortable: i,
                            rootEl: n,
                            name: "select",
                            targetEl: le,
                            originalEvent: e
                        }), e.shiftKey && re && i.el.contains(re)) {
                            var p = j(re), t = j(le);
                            if (~p && ~t && p !== t) for (var g, m = p < t ? (g = p, t) : (g = t, p + 1); g < m; g++) ~ue.indexOf(f[g]) || (k(f[g], h.selectedClass, !0), ue.push(f[g]), z({
                                sortable: i,
                                rootEl: n,
                                name: "select",
                                targetEl: f[g],
                                originalEvent: e
                            }))
                        } else re = le;
                        ae = s
                    }
                    pe && this.isMultiDrag && (fe = !1, (o[K].options.sort || o !== n) && 1 < ue.length && (c = X(le), u = j(le, ":not(." + this.options.selectedClass + ")"), !he && h.animation && (le.thisAnimationDuration = null), s.captureAnimationState(), he || (h.animation && (le.fromRect = c, ue.forEach(function (t) {
                        var e;
                        t.thisAnimationDuration = null, t !== le && (e = fe ? X(t) : c, t.fromRect = e, s.addAnimationState({
                            target: t,
                            rect: e
                        }))
                    })), me(), ue.forEach(function (t) {
                        f[u] ? o.insertBefore(t, f[u]) : o.appendChild(t), u++
                    }), a === j(le) && (d = !1, ue.forEach(function (t) {
                        t.sortableIndex !== j(t) && (d = !0)
                    }), d && (r("update"), r("sort")))), ue.forEach(function (t) {
                        T(t)
                    }), s.animateAll()), ae = s), (n === o || l && "clone" !== l.lastPutMode) && de.forEach(function (t) {
                        t.parentNode && t.parentNode.removeChild(t)
                    })
                }
            }, nullingGlobal: function () {
                this.isMultiDrag = pe = !1, de.length = 0
            }, destroyGlobal: function () {
                this._deselectMultiDrag(), f(document, "pointerup", this._deselectMultiDrag), f(document, "mouseup", this._deselectMultiDrag), f(document, "touchend", this._deselectMultiDrag), f(document, "keydown", this._checkKeyDown), f(document, "keyup", this._checkKeyUp)
            }, _deselectMultiDrag: function (t) {
                if (!(void 0 !== pe && pe || ae !== this.sortable || t && P(t.target, this.options.draggable, this.sortable.el, !1) || t && 0 !== t.button)) for (; ue.length;) {
                    var e = ue[0];
                    k(e, this.options.selectedClass, !1), ue.shift(), z({
                        sortable: this.sortable,
                        rootEl: this.sortable.el,
                        name: "deselect",
                        targetEl: e,
                        originalEvent: t
                    })
                }
            }, _checkKeyDown: function (t) {
                t.key === this.options.multiDragKey && (this.multiDragKeyDown = !0)
            }, _checkKeyUp: function (t) {
                t.key === this.options.multiDragKey && (this.multiDragKeyDown = !1)
            }
        }, a(t, {
            pluginName: "multiDrag", utils: {
                select: function (t) {
                    var e = t.parentNode[K];
                    e && e.options.multiDrag && !~ue.indexOf(t) && (ae && ae !== e && (ae.multiDrag._deselectMultiDrag(), ae = e), k(t, e.options.selectedClass, !0), ue.push(t))
                }, deselect: function (t) {
                    var e = t.parentNode[K], n = ue.indexOf(t);
                    e && e.options.multiDrag && ~n && (k(t, e.options.selectedClass, !1), ue.splice(n, 1))
                }
            }, eventProperties: function () {
                var n = this, o = [], i = [];
                return ue.forEach(function (t) {
                    var e;
                    o.push({
                        multiDragElement: t,
                        index: t.sortableIndex
                    }), e = fe && t !== le ? -1 : fe ? j(t, ":not(." + n.options.selectedClass + ")") : j(t), i.push({
                        multiDragElement: t,
                        index: e
                    })
                }), {items: r(ue), clones: [].concat(de), oldIndicies: o, newIndicies: i}
            }, optionListeners: {
                multiDragKey: function (t) {
                    return "ctrl" === (t = t.toLowerCase()) ? t = "Control" : 1 < t.length && (t = t.charAt(0).toUpperCase() + t.substr(1)), t
                }
            }
        })
    }), Ft
});