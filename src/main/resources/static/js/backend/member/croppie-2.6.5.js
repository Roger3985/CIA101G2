!function (e, t) {
    "function" == typeof define && define.amd ? define(t) : "object" == typeof exports && "string" != typeof exports.nodeName ? module.exports = t() : e.Croppie = t()
}("undefined" != typeof self ? self : this, function () {
    "function" != typeof Promise && function (e) {
        function t(e, t) {
            return function () {
                e.apply(t, arguments)
            }
        }

        function i(e) {
            if ("object" != typeof this) throw new TypeError("Promises must be constructed via new");
            if ("function" != typeof e) throw new TypeError("not a function");
            this._state = null, this._value = null, this._deferreds = [], s(e, t(o, this), t(r, this))
        }

        function n(e) {
            var t = this;
            return null === this._state ? void this._deferreds.push(e) : void h(function () {
                var i = t._state ? e.onFulfilled : e.onRejected;
                if (null !== i) {
                    var n;
                    try {
                        n = i(t._value)
                    } catch (t) {
                        return void e.reject(t)
                    }
                    e.resolve(n)
                } else (t._state ? e.resolve : e.reject)(t._value)
            })
        }

        function o(e) {
            try {
                if (e === this) throw new TypeError("A promise cannot be resolved with itself.");
                if (e && ("object" == typeof e || "function" == typeof e)) {
                    var i = e.then;
                    if ("function" == typeof i) return void s(t(i, e), t(o, this), t(r, this))
                }
                this._state = !0, this._value = e, a.call(this)
            } catch (e) {
                r.call(this, e)
            }
        }

        function r(e) {
            this._state = !1, this._value = e, a.call(this)
        }

        function a() {
            for (var e = 0, t = this._deferreds.length; t > e; e++) n.call(this, this._deferreds[e]);
            this._deferreds = null
        }

        function s(e, t, i) {
            var n = !1;
            try {
                e(function (e) {
                    n || (n = !0, t(e))
                }, function (e) {
                    n || (n = !0, i(e))
                })
            } catch (e) {
                if (n) return;
                n = !0, i(e)
            }
        }

        var l = setTimeout, h = "function" == typeof setImmediate && setImmediate || function (e) {
            l(e, 1)
        }, u = Array.isArray || function (e) {
            return "[object Array]" === Object.prototype.toString.call(e)
        };
        i.prototype.catch = function (e) {
            return this.then(null, e)
        }, i.prototype.then = function (e, t) {
            var o = this;
            return new i(function (i, r) {
                n.call(o, new function (e, t, i, n) {
                    this.onFulfilled = "function" == typeof e ? e : null, this.onRejected = "function" == typeof t ? t : null, this.resolve = i, this.reject = n
                }(e, t, i, r))
            })
        }, i.all = function () {
            var e = Array.prototype.slice.call(1 === arguments.length && u(arguments[0]) ? arguments[0] : arguments);
            return new i(function (t, i) {
                function n(r, a) {
                    try {
                        if (a && ("object" == typeof a || "function" == typeof a)) {
                            var s = a.then;
                            if ("function" == typeof s) return void s.call(a, function (e) {
                                n(r, e)
                            }, i)
                        }
                        e[r] = a, 0 == --o && t(e)
                    } catch (e) {
                        i(e)
                    }
                }

                if (0 === e.length) return t([]);
                for (var o = e.length, r = 0; r < e.length; r++) n(r, e[r])
            })
        }, i.resolve = function (e) {
            return e && "object" == typeof e && e.constructor === i ? e : new i(function (t) {
                t(e)
            })
        }, i.reject = function (e) {
            return new i(function (t, i) {
                i(e)
            })
        }, i.race = function (e) {
            return new i(function (t, i) {
                for (var n = 0, o = e.length; o > n; n++) e[n].then(t, i)
            })
        }, i._setImmediateFn = function (e) {
            h = e
        }, "undefined" != typeof module && module.exports ? module.exports = i : e.Promise || (e.Promise = i)
    }(this), "undefined" != typeof window && "function" != typeof window.CustomEvent && function () {
        function e(e, t) {
            t = t || {bubbles: !1, cancelable: !1, detail: void 0};
            var i = document.createEvent("CustomEvent");
            return i.initCustomEvent(e, t.bubbles, t.cancelable, t.detail), i
        }

        e.prototype = window.Event.prototype, window.CustomEvent = e
    }(), "undefined" == typeof HTMLCanvasElement || HTMLCanvasElement.prototype.toBlob || Object.defineProperty(HTMLCanvasElement.prototype, "toBlob", {
        value: function (e, t, i) {
            for (var n = atob(this.toDataURL(t, i).split(",")[1]), o = n.length, r = new Uint8Array(o), a = 0; a < o; a++) r[a] = n.charCodeAt(a);
            e(new Blob([r], {type: t || "image/png"}))
        }
    });
    var e, t, i, n = ["Webkit", "Moz", "ms"],
        o = "undefined" != typeof document ? document.createElement("div").style : {}, r = [1, 8, 3, 6],
        a = [2, 7, 4, 5];

    function s(e) {
        if (e in o) return e;
        for (var t = e[0].toUpperCase() + e.slice(1), i = n.length; i--;) if ((e = n[i] + t) in o) return e
    }

    function l(e, t) {
        e = e || {};
        for (var i in t) t[i] && t[i].constructor && t[i].constructor === Object ? (e[i] = e[i] || {}, l(e[i], t[i])) : e[i] = t[i];
        return e
    }

    function h(e) {
        return l({}, e)
    }

    function u(e) {
        if ("createEvent" in document) {
            var t = document.createEvent("HTMLEvents");
            t.initEvent("change", !1, !0), e.dispatchEvent(t)
        } else e.fireEvent("onchange")
    }

    function c(e, t, i) {
        if ("string" == typeof t) {
            var n = t;
            (t = {})[n] = i
        }
        for (var o in t) e.style[o] = t[o]
    }

    function p(e, t) {
        e.classList ? e.classList.add(t) : e.className += " " + t
    }

    function d(e, t) {
        for (var i in t) e.setAttribute(i, t[i])
    }

    function m(e) {
        return parseInt(e, 10)
    }

    function f(e, t) {
        var i = e.naturalWidth, n = e.naturalHeight, o = t || y(e);
        if (o && o >= 5) {
            var r = i;
            i = n, n = r
        }
        return {width: i, height: n}
    }

    t = s("transform"), e = s("transformOrigin"), i = s("userSelect");
    var v = {translate3d: {suffix: ", 0px"}, translate: {suffix: ""}}, g = function (e, t, i) {
        this.x = parseFloat(e), this.y = parseFloat(t), this.scale = parseFloat(i)
    };
    g.parse = function (e) {
        return e.style ? g.parse(e.style[t]) : e.indexOf("matrix") > -1 || e.indexOf("none") > -1 ? g.fromMatrix(e) : g.fromString(e)
    }, g.fromMatrix = function (e) {
        var t = e.substring(7).split(",");
        return t.length && "none" !== e || (t = [1, 0, 0, 1, 0, 0]), new g(m(t[4]), m(t[5]), parseFloat(t[0]))
    }, g.fromString = function (e) {
        var t = e.split(") "), i = t[0].substring(T.globals.translate.length + 1).split(","),
            n = t.length > 1 ? t[1].substring(6) : 1, o = i.length > 1 ? i[0] : 0, r = i.length > 1 ? i[1] : 0;
        return new g(o, r, n)
    }, g.prototype.toString = function () {
        var e = v[T.globals.translate].suffix || "";
        return T.globals.translate + "(" + this.x + "px, " + this.y + "px" + e + ") scale(" + this.scale + ")"
    };
    var w = function (t) {
        if (!t || !t.style[e]) return this.x = 0, void (this.y = 0);
        var i = t.style[e].split(" ");
        this.x = parseFloat(i[0]), this.y = parseFloat(i[1])
    };

    function y(e) {
        return e.exifdata && e.exifdata.Orientation ? m(e.exifdata.Orientation) : 1
    }

    function b(e, t, i) {
        var n = t.width, o = t.height, r = e.getContext("2d");
        switch (e.width = t.width, e.height = t.height, r.save(), i) {
            case 2:
                r.translate(n, 0), r.scale(-1, 1);
                break;
            case 3:
                r.translate(n, o), r.rotate(180 * Math.PI / 180);
                break;
            case 4:
                r.translate(0, o), r.scale(1, -1);
                break;
            case 5:
                e.width = o, e.height = n, r.rotate(90 * Math.PI / 180), r.scale(1, -1);
                break;
            case 6:
                e.width = o, e.height = n, r.rotate(90 * Math.PI / 180), r.translate(0, -o);
                break;
            case 7:
                e.width = o, e.height = n, r.rotate(-90 * Math.PI / 180), r.translate(-n, o), r.scale(1, -1);
                break;
            case 8:
                e.width = o, e.height = n, r.translate(0, n), r.rotate(-90 * Math.PI / 180)
        }
        r.drawImage(t, 0, 0, n, o), r.restore()
    }

    function x() {
        var n, o, r, a, s, l, h = this.options.viewport.type ? "cr-vp-" + this.options.viewport.type : null;
        this.options.useCanvas = this.options.enableOrientation || C.call(this), this.data = {}, this.elements = {}, n = this.elements.boundary = document.createElement("div"), r = this.elements.viewport = document.createElement("div"), o = this.elements.img = document.createElement("img"), a = this.elements.overlay = document.createElement("div"), this.options.useCanvas ? (this.elements.canvas = document.createElement("canvas"), this.elements.preview = this.elements.canvas) : this.elements.preview = o, p(n, "cr-boundary"), n.setAttribute("aria-dropeffect", "none"), s = this.options.boundary.width, l = this.options.boundary.height, c(n, {
            width: s + (isNaN(s) ? "" : "px"),
            height: l + (isNaN(l) ? "" : "px")
        }), p(r, "cr-viewport"), h && p(r, h), c(r, {
            width: this.options.viewport.width + "px",
            height: this.options.viewport.height + "px"
        }), r.setAttribute("tabindex", 0), p(this.elements.preview, "cr-image"), d(this.elements.preview, {
            alt: "preview",
            "aria-grabbed": "false"
        }), p(a, "cr-overlay"), this.element.appendChild(n), n.appendChild(this.elements.preview), n.appendChild(r), n.appendChild(a), p(this.element, "croppie-container"), this.options.customClass && p(this.element, this.options.customClass), function () {
            var e, n, o, r, a, s = this, l = !1;

            function h(e, t) {
                var i = s.elements.preview.getBoundingClientRect(), n = a.y + t, o = a.x + e;
                s.options.enforceBoundary ? (r.top > i.top + t && r.bottom < i.bottom + t && (a.y = n), r.left > i.left + e && r.right < i.right + e && (a.x = o)) : (a.y = n, a.x = o)
            }

            function p(e) {
                s.elements.preview.setAttribute("aria-grabbed", e), s.elements.boundary.setAttribute("aria-dropeffect", e ? "move" : "none")
            }

            function d(t) {
                if ((void 0 === t.button || 0 === t.button) && (t.preventDefault(), !l)) {
                    if (l = !0, e = t.pageX, n = t.pageY, t.touches) {
                        var o = t.touches[0];
                        e = o.pageX, n = o.pageY
                    }
                    p(l), a = g.parse(s.elements.preview), window.addEventListener("mousemove", m), window.addEventListener("touchmove", m), window.addEventListener("mouseup", f), window.addEventListener("touchend", f), document.body.style[i] = "none", r = s.elements.viewport.getBoundingClientRect()
                }
            }

            function m(i) {
                i.preventDefault();
                var r = i.pageX, l = i.pageY;
                if (i.touches) {
                    var p = i.touches[0];
                    r = p.pageX, l = p.pageY
                }
                var d = r - e, m = l - n, f = {};
                if ("touchmove" === i.type && i.touches.length > 1) {
                    var v = i.touches[0], g = i.touches[1],
                        w = Math.sqrt((v.pageX - g.pageX) * (v.pageX - g.pageX) + (v.pageY - g.pageY) * (v.pageY - g.pageY));
                    o || (o = w / s._currentZoom);
                    var y = w / o;
                    return E.call(s, y), void u(s.elements.zoomer)
                }
                h(d, m), f[t] = a.toString(), c(s.elements.preview, f), L.call(s), n = l, e = r
            }

            function f() {
                p(l = !1), window.removeEventListener("mousemove", m), window.removeEventListener("touchmove", m), window.removeEventListener("mouseup", f), window.removeEventListener("touchend", f), document.body.style[i] = "", _.call(s), z.call(s), o = 0
            }

            s.elements.overlay.addEventListener("mousedown", d), s.elements.viewport.addEventListener("keydown", function (e) {
                var n = 37, l = 38, u = 39, p = 40;
                if (!e.shiftKey || e.keyCode !== l && e.keyCode !== p) {
                    if (s.options.enableKeyMovement && e.keyCode >= 37 && e.keyCode <= 40) {
                        e.preventDefault();
                        var d = function (e) {
                            switch (e) {
                                case n:
                                    return [1, 0];
                                case l:
                                    return [0, 1];
                                case u:
                                    return [-1, 0];
                                case p:
                                    return [0, -1]
                            }
                        }(e.keyCode);
                        a = g.parse(s.elements.preview), document.body.style[i] = "none", r = s.elements.viewport.getBoundingClientRect(), function (e) {
                            var n = e[0], r = e[1], l = {};
                            h(n, r), l[t] = a.toString(), c(s.elements.preview, l), L.call(s), document.body.style[i] = "", _.call(s), z.call(s), o = 0
                        }(d)
                    }
                } else {
                    var m;
                    m = e.keyCode === l ? parseFloat(s.elements.zoomer.value) + parseFloat(s.elements.zoomer.step) : parseFloat(s.elements.zoomer.value) - parseFloat(s.elements.zoomer.step), s.setZoom(m)
                }
            }), s.elements.overlay.addEventListener("touchstart", d)
        }.call(this), this.options.enableZoom && function () {
            var i = this, n = i.elements.zoomerWrap = document.createElement("div"),
                o = i.elements.zoomer = document.createElement("input");

            function r() {
                (function (i) {
                    var n = this, o = i ? i.transform : g.parse(n.elements.preview),
                        r = i ? i.viewportRect : n.elements.viewport.getBoundingClientRect(),
                        a = i ? i.origin : new w(n.elements.preview);

                    function s() {
                        var i = {};
                        i[t] = o.toString(), i[e] = a.toString(), c(n.elements.preview, i)
                    }

                    if (n._currentZoom = i ? i.value : n._currentZoom, o.scale = n._currentZoom, n.elements.zoomer.setAttribute("aria-valuenow", n._currentZoom), s(), n.options.enforceBoundary) {
                        var l = function (e) {
                            var t = this._currentZoom, i = e.width, n = e.height,
                                o = this.elements.boundary.clientWidth / 2, r = this.elements.boundary.clientHeight / 2,
                                a = this.elements.preview.getBoundingClientRect(), s = a.width, l = a.height, h = i / 2,
                                u = n / 2, c = -1 * (h / t - o), p = -1 * (u / t - r), d = 1 / t * h, m = 1 / t * u;
                            return {
                                translate: {
                                    maxX: c,
                                    minX: c - (s * (1 / t) - i * (1 / t)),
                                    maxY: p,
                                    minY: p - (l * (1 / t) - n * (1 / t))
                                }, origin: {maxX: s * (1 / t) - d, minX: d, maxY: l * (1 / t) - m, minY: m}
                            }
                        }.call(n, r), h = l.translate, u = l.origin;
                        o.x >= h.maxX && (a.x = u.minX, o.x = h.maxX), o.x <= h.minX && (a.x = u.maxX, o.x = h.minX), o.y >= h.maxY && (a.y = u.minY, o.y = h.maxY), o.y <= h.minY && (a.y = u.maxY, o.y = h.minY)
                    }
                    s(), M.call(n), z.call(n)
                }).call(i, {
                    value: parseFloat(o.value),
                    origin: new w(i.elements.preview),
                    viewportRect: i.elements.viewport.getBoundingClientRect(),
                    transform: g.parse(i.elements.preview)
                })
            }

            function a(e) {
                var t, n;
                if ("ctrl" === i.options.mouseWheelZoom && !0 !== e.ctrlKey) return 0;
                t = e.wheelDelta ? e.wheelDelta / 1200 : e.deltaY ? e.deltaY / 1060 : e.detail ? e.detail / -60 : 0, n = i._currentZoom + t * i._currentZoom, e.preventDefault(), E.call(i, n), r.call(i)
            }

            p(n, "cr-slider-wrap"), p(o, "cr-slider"), o.type = "range", o.step = "0.0001", o.value = "1", o.style.display = i.options.showZoomer ? "" : "none", o.setAttribute("aria-label", "zoom"), i.element.appendChild(n), n.appendChild(o), i._currentZoom = 1, i.elements.zoomer.addEventListener("input", r), i.elements.zoomer.addEventListener("change", r), i.options.mouseWheelZoom && (i.elements.boundary.addEventListener("mousewheel", a), i.elements.boundary.addEventListener("DOMMouseScroll", a))
        }.call(this), this.options.enableResize && function () {
            var e, t, n, o, r, a, s, l = this, h = document.createElement("div"), u = !1, d = 50;
            p(h, "cr-resizer"), c(h, {
                width: this.options.viewport.width + "px",
                height: this.options.viewport.height + "px"
            }), this.options.resizeControls.height && (p(a = document.createElement("div"), "cr-resizer-vertical"), h.appendChild(a));
            this.options.resizeControls.width && (p(s = document.createElement("div"), "cr-resizer-horisontal"), h.appendChild(s));

            function m(a) {
                if ((void 0 === a.button || 0 === a.button) && (a.preventDefault(), !u)) {
                    var s = l.elements.overlay.getBoundingClientRect();
                    if (u = !0, t = a.pageX, n = a.pageY, e = -1 !== a.currentTarget.className.indexOf("vertical") ? "v" : "h", o = s.width, r = s.height, a.touches) {
                        var h = a.touches[0];
                        t = h.pageX, n = h.pageY
                    }
                    window.addEventListener("mousemove", f), window.addEventListener("touchmove", f), window.addEventListener("mouseup", v), window.addEventListener("touchend", v), document.body.style[i] = "none"
                }
            }

            function f(i) {
                var a = i.pageX, s = i.pageY;
                if (i.preventDefault(), i.touches) {
                    var u = i.touches[0];
                    a = u.pageX, s = u.pageY
                }
                var p = a - t, m = s - n, f = l.options.viewport.height + m, v = l.options.viewport.width + p;
                "v" === e && f >= d && f <= r ? (c(h, {height: f + "px"}), l.options.boundary.height += m, c(l.elements.boundary, {height: l.options.boundary.height + "px"}), l.options.viewport.height += m, c(l.elements.viewport, {height: l.options.viewport.height + "px"})) : "h" === e && v >= d && v <= o && (c(h, {width: v + "px"}), l.options.boundary.width += p, c(l.elements.boundary, {width: l.options.boundary.width + "px"}), l.options.viewport.width += p, c(l.elements.viewport, {width: l.options.viewport.width + "px"})), L.call(l), X.call(l), _.call(l), z.call(l), n = s, t = a
            }

            function v() {
                u = !1, window.removeEventListener("mousemove", f), window.removeEventListener("touchmove", f), window.removeEventListener("mouseup", v), window.removeEventListener("touchend", v), document.body.style[i] = ""
            }

            a && (a.addEventListener("mousedown", m), a.addEventListener("touchstart", m));
            s && (s.addEventListener("mousedown", m), s.addEventListener("touchstart", m));
            this.elements.boundary.appendChild(h)
        }.call(this)
    }

    function C() {
        return this.options.enableExif && window.EXIF
    }

    function E(e) {
        if (this.options.enableZoom) {
            var t = this.elements.zoomer, i = O(e, 4);
            t.value = Math.max(parseFloat(t.min), Math.min(parseFloat(t.max), i)).toString()
        }
    }

    function _(i) {
        var n = this._currentZoom, o = this.elements.preview.getBoundingClientRect(),
            r = this.elements.viewport.getBoundingClientRect(), a = g.parse(this.elements.preview.style[t]),
            s = new w(this.elements.preview), l = r.top - o.top + r.height / 2, h = r.left - o.left + r.width / 2,
            u = {}, p = {};
        if (i) {
            var d = s.x, m = s.y, f = a.x, v = a.y;
            u.y = d, u.x = m, a.y = f, a.x = v
        } else u.y = l / n, u.x = h / n, p.y = (u.y - s.y) * (1 - n), p.x = (u.x - s.x) * (1 - n), a.x -= p.x, a.y -= p.y;
        var y = {};
        y[e] = u.x + "px " + u.y + "px", y[t] = a.toString(), c(this.elements.preview, y)
    }

    function L() {
        if (this.elements) {
            var e = this.elements.boundary.getBoundingClientRect(), t = this.elements.preview.getBoundingClientRect();
            c(this.elements.overlay, {
                width: t.width + "px",
                height: t.height + "px",
                top: t.top - e.top + "px",
                left: t.left - e.left + "px"
            })
        }
    }

    w.prototype.toString = function () {
        return this.x + "px " + this.y + "px"
    };
    var R, B, Z, I, M = (R = L, B = 500, function () {
        var e = this, t = arguments, i = Z && !I;
        clearTimeout(I), I = setTimeout(function () {
            I = null, Z || R.apply(e, t)
        }, B), i && R.apply(e, t)
    });

    function z() {
        var e, t = this.get();
        F.call(this) && (this.options.update.call(this, t), this.$ && "undefined" == typeof Prototype ? this.$(this.element).trigger("update.croppie", t) : (window.CustomEvent ? e = new CustomEvent("update", {detail: t}) : (e = document.createEvent("CustomEvent")).initCustomEvent("update", !0, !0, t), this.element.dispatchEvent(e)))
    }

    function F() {
        return this.elements.preview.offsetHeight > 0 && this.elements.preview.offsetWidth > 0
    }

    function W() {
        var i, n = {}, o = this.elements.preview, r = new g(0, 0, 1), a = new w;
        F.call(this) && !this.data.bound && (this.data.bound = !0, n[t] = r.toString(), n[e] = a.toString(), n.opacity = 1, c(o, n), i = this.elements.preview.getBoundingClientRect(), this._originalImageWidth = i.width, this._originalImageHeight = i.height, this.data.orientation = C.call(this) ? y(this.elements.img) : this.data.orientation, this.options.enableZoom ? X.call(this, !0) : this._currentZoom = 1, r.scale = this._currentZoom, n[t] = r.toString(), c(o, n), this.data.points.length ? function (i) {
            if (4 !== i.length) throw "Croppie - Invalid number of points supplied: " + i;
            var n = i[2] - i[0], o = this.elements.viewport.getBoundingClientRect(),
                r = this.elements.boundary.getBoundingClientRect(), a = {left: o.left - r.left, top: o.top - r.top},
                s = o.width / n, l = i[1], h = i[0], u = -1 * i[1] + a.top, p = -1 * i[0] + a.left, d = {};
            d[e] = h + "px " + l + "px", d[t] = new g(p, u, s).toString(), c(this.elements.preview, d), E.call(this, s), this._currentZoom = s
        }.call(this, this.data.points) : function () {
            var e = this.elements.preview.getBoundingClientRect(), i = this.elements.viewport.getBoundingClientRect(),
                n = this.elements.boundary.getBoundingClientRect(), o = i.left - n.left, r = i.top - n.top,
                a = o - (e.width - i.width) / 2, s = r - (e.height - i.height) / 2, l = new g(a, s, this._currentZoom);
            c(this.elements.preview, t, l.toString())
        }.call(this), _.call(this), L.call(this))
    }

    function X(e) {
        var t, i, n, o, r = Math.max(this.options.minZoom, 0) || 0, a = this.options.maxZoom || 1.5,
            s = this.elements.zoomer, l = parseFloat(s.value), h = this.elements.boundary.getBoundingClientRect(),
            c = f(this.elements.img, this.data.orientation), p = this.elements.viewport.getBoundingClientRect();
        this.options.enforceBoundary && (n = p.width / c.width, o = p.height / c.height, r = Math.max(n, o)), r >= a && (a = r + 1), s.min = O(r, 4), s.max = O(a, 4), !e && (l < s.min || l > s.max) ? E.call(this, l < s.min ? s.min : s.max) : e && (i = Math.max(h.width / c.width, h.height / c.height), t = null !== this.data.boundZoom ? this.data.boundZoom : i, E.call(this, t)), u(s)
    }

    function Y(e) {
        var t = e.points, i = m(t[0]), n = m(t[1]), o = m(t[2]) - i, r = m(t[3]) - n, a = e.circle,
            s = document.createElement("canvas"), l = s.getContext("2d"), h = e.outputWidth || o,
            u = e.outputHeight || r;
        s.width = h, s.height = u, e.backgroundColor && (l.fillStyle = e.backgroundColor, l.fillRect(0, 0, h, u));
        var c = i, p = n, d = o, f = r, v = 0, g = 0, w = h, y = u;
        return i < 0 && (c = 0, v = Math.abs(i) / o * h), d + c > this._originalImageWidth && (w = (d = this._originalImageWidth - c) / o * h), n < 0 && (p = 0, g = Math.abs(n) / r * u), f + p > this._originalImageHeight && (y = (f = this._originalImageHeight - p) / r * u), l.drawImage(this.elements.preview, c, p, d, f, v, g, w, y), a && (l.fillStyle = "#fff", l.globalCompositeOperation = "destination-in", l.beginPath(), l.arc(s.width / 2, s.height / 2, s.width / 2, 0, 2 * Math.PI, !0), l.closePath(), l.fill()), s
    }

    function H(e, t) {
        var i, n = this, o = [], r = null, a = C.call(n);
        if ("string" == typeof e) i = e, e = {}; else if (Array.isArray(e)) o = e.slice(); else {
            if (void 0 === e && n.data.url) return W.call(n), z.call(n), null;
            i = e.url, o = e.points || [], r = void 0 === e.zoom ? null : e.zoom
        }
        return n.data.bound = !1, n.data.url = i || n.data.url, n.data.boundZoom = r, function (e, t) {
            if (!e) throw "Source image missing";
            var i = new Image;
            return i.style.opacity = "0", new Promise(function (n, o) {
                function r() {
                    i.style.opacity = "1", setTimeout(function () {
                        n(i)
                    }, 1)
                }

                i.removeAttribute("crossOrigin"), e.match(/^https?:\/\/|^\/\//) && i.setAttribute("crossOrigin", "anonymous"), i.onload = function () {
                    t ? EXIF.getData(i, function () {
                        r()
                    }) : r()
                }, i.onerror = function (e) {
                    i.style.opacity = 1, setTimeout(function () {
                        o(e)
                    }, 1)
                }, i.src = e
            })
        }(i, a).then(function (i) {
            if (function (e) {
                this.elements.img.parentNode && (Array.prototype.forEach.call(this.elements.img.classList, function (t) {
                    e.classList.add(t)
                }), this.elements.img.parentNode.replaceChild(e, this.elements.img), this.elements.preview = e), this.elements.img = e
            }.call(n, i), o.length) n.options.relative && (o = [o[0] * i.naturalWidth / 100, o[1] * i.naturalHeight / 100, o[2] * i.naturalWidth / 100, o[3] * i.naturalHeight / 100]); else {
                var r, a, s = f(i), l = n.elements.viewport.getBoundingClientRect(), h = l.width / l.height;
                s.width / s.height > h ? r = (a = s.height) * h : (r = s.width, a = s.height / h);
                var u = (s.width - r) / 2, c = (s.height - a) / 2, p = u + r, d = c + a;
                n.data.points = [u, c, p, d]
            }
            n.data.orientation = e.orientation || 1, n.data.points = o.map(function (e) {
                return parseFloat(e)
            }), n.options.useCanvas && function (e) {
                var t = this.elements.canvas, i = this.elements.img;
                t.getContext("2d").clearRect(0, 0, t.width, t.height), t.width = i.width, t.height = i.height, b(t, i, this.options.enableOrientation && e || y(i))
            }.call(n, n.data.orientation), W.call(n), z.call(n), t && t()
        })
    }

    function O(e, t) {
        return parseFloat(e).toFixed(t || 0)
    }

    function k() {
        var e = this.elements.preview.getBoundingClientRect(), t = this.elements.viewport.getBoundingClientRect(),
            i = t.left - e.left, n = t.top - e.top, o = (t.width - this.elements.viewport.offsetWidth) / 2,
            r = (t.height - this.elements.viewport.offsetHeight) / 2, a = i + this.elements.viewport.offsetWidth + o,
            s = n + this.elements.viewport.offsetHeight + r, l = this._currentZoom;
        (l === 1 / 0 || isNaN(l)) && (l = 1);
        var h = this.options.enforceBoundary ? 0 : Number.NEGATIVE_INFINITY;
        return i = Math.max(h, i / l), n = Math.max(h, n / l), a = Math.max(h, a / l), s = Math.max(h, s / l), {
            points: [O(i), O(n), O(a), O(s)],
            zoom: l,
            orientation: this.data.orientation
        }
    }

    var A = {type: "canvas", format: "png", quality: 1}, S = ["jpeg", "webp", "png"];

    function j(e) {
        var t = this, i = k.call(t), n = l(h(A), h(e)), o = "string" == typeof e ? e : n.type || "base64",
            r = n.size || "viewport", a = n.format, s = n.quality, u = n.backgroundColor,
            d = "boolean" == typeof n.circle ? n.circle : "circle" === t.options.viewport.type,
            m = t.elements.viewport.getBoundingClientRect(), f = m.width / m.height;
        return "viewport" === r ? (i.outputWidth = m.width, i.outputHeight = m.height) : "object" == typeof r && (r.width && r.height ? (i.outputWidth = r.width, i.outputHeight = r.height) : r.width ? (i.outputWidth = r.width, i.outputHeight = r.width / f) : r.height && (i.outputWidth = r.height * f, i.outputHeight = r.height)), S.indexOf(a) > -1 && (i.format = "image/" + a, i.quality = s), i.circle = d, i.url = t.data.url, i.backgroundColor = u, new Promise(function (e) {
            switch (o.toLowerCase()) {
                case"rawcanvas":
                    e(Y.call(t, i));
                    break;
                case"canvas":
                case"base64":
                    e(function (e) {
                        return Y.call(this, e).toDataURL(e.format, e.quality)
                    }.call(t, i));
                    break;
                case"blob":
                    (function (e) {
                        var t = this;
                        return new Promise(function (i) {
                            Y.call(t, e).toBlob(function (e) {
                                i(e)
                            }, e.format, e.quality)
                        })
                    }).call(t, i).then(e);
                    break;
                default:
                    e(function (e) {
                        var t = e.points, i = document.createElement("div"), n = document.createElement("img"),
                            o = t[2] - t[0], r = t[3] - t[1];
                        return p(i, "croppie-result"), i.appendChild(n), c(n, {
                            left: -1 * t[0] + "px",
                            top: -1 * t[1] + "px"
                        }), n.src = e.url, c(i, {width: o + "px", height: r + "px"}), i
                    }.call(t, i))
            }
        })
    }

    function N(e) {
        if (!this.options.useCanvas || !this.options.enableOrientation) throw "Croppie: Cannot rotate without enableOrientation && EXIF.js included";
        var t, i, n, o, s, l = this.elements.canvas;
        if (this.data.orientation = (t = this.data.orientation, i = e, n = r.indexOf(t) > -1 ? r : a, o = n.indexOf(t), s = i / 90 % n.length, n[(n.length + o + s % n.length) % n.length]), b(l, this.elements.img, this.data.orientation), _.call(this, !0), X.call(this), Math.abs(e) / 90 % 2 == 1) {
            var h = this._originalImageHeight, u = this._originalImageWidth;
            this._originalImageWidth = h, this._originalImageHeight = u
        }
    }

    if ("undefined" != typeof window && window.jQuery) {
        var P = window.jQuery;
        P.fn.croppie = function (e) {
            if ("string" === typeof e) {
                var t = Array.prototype.slice.call(arguments, 1), i = P(this).data("croppie");
                return "get" === e ? i.get() : "result" === e ? i.result.apply(i, t) : "bind" === e ? i.bind.apply(i, t) : this.each(function () {
                    var i = P(this).data("croppie");
                    if (i) {
                        var n = i[e];
                        if (!P.isFunction(n)) throw "Croppie " + e + " method not found";
                        n.apply(i, t), "destroy" === e && P(this).removeData("croppie")
                    }
                })
            }
            return this.each(function () {
                var t = new T(this, e);
                t.$ = P, P(this).data("croppie", t)
            })
        }
    }

    function T(e, t) {
        if (e.className.indexOf("croppie-container") > -1) throw new Error("Croppie: Can't initialize croppie more than once");
        if (this.element = e, this.options = l(h(T.defaults), t), "img" === this.element.tagName.toLowerCase()) {
            var i = this.element;
            p(i, "cr-original-image"), d(i, {"aria-hidden": "true", alt: ""});
            var n = document.createElement("div");
            this.element.parentNode.appendChild(n), n.appendChild(i), this.element = n, this.options.url = this.options.url || i.src
        }
        if (x.call(this), this.options.url) {
            var o = {url: this.options.url, points: this.options.points};
            delete this.options.url, delete this.options.points, H.call(this, o)
        }
    }

    return T.defaults = {
        viewport: {width: 100, height: 100, type: "square"},
        boundary: {},
        orientationControls: {enabled: !0, leftClass: "", rightClass: ""},
        resizeControls: {width: !0, height: !0},
        customClass: "",
        showZoomer: !0,
        enableZoom: !0,
        enableResize: !1,
        mouseWheelZoom: !0,
        enableExif: !1,
        enforceBoundary: !0,
        enableOrientation: !1,
        enableKeyMovement: !0,
        update: function () {
        }
    }, T.globals = {translate: "translate3d"}, l(T.prototype, {
        bind: function (e, t) {
            return H.call(this, e, t)
        }, get: function () {
            var e = k.call(this), t = e.points;
            return this.options.relative && (t[0] /= this.elements.img.naturalWidth / 100, t[1] /= this.elements.img.naturalHeight / 100, t[2] /= this.elements.img.naturalWidth / 100, t[3] /= this.elements.img.naturalHeight / 100), e
        }, result: function (e) {
            return j.call(this, e)
        }, refresh: function () {
            return function () {
                W.call(this)
            }.call(this)
        }, setZoom: function (e) {
            E.call(this, e), u(this.elements.zoomer)
        }, rotate: function (e) {
            N.call(this, e)
        }, destroy: function () {
            return function () {
                var e, t;
                this.element.removeChild(this.elements.boundary), e = this.element, t = "croppie-container", e.classList ? e.classList.remove(t) : e.className = e.className.replace(t, ""), this.options.enableZoom && this.element.removeChild(this.elements.zoomerWrap), delete this.elements
            }.call(this)
        }
    }), T
});