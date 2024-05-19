/* sockjs-client v1.5.1 | http://sockjs.org | MIT license */
!function (e) {
    if ("object" == typeof exports && "undefined" != typeof module) module.exports = e(); else if ("function" == typeof define && define.amd) define([], e); else {
        ("undefined" != typeof window ? window : "undefined" != typeof global ? global : "undefined" != typeof self ? self : this).SockJS = e()
    }
}(function () {
    return function i(s, a, l) {
        function u(t, e) {
            if (!a[t]) {
                if (!s[t]) {
                    var n = "function" == typeof require && require;
                    if (!e && n) return n(t, !0);
                    if (c) return c(t, !0);
                    var r = new Error("Cannot find module '" + t + "'");
                    throw r.code = "MODULE_NOT_FOUND", r
                }
                var o = a[t] = {exports: {}};
                s[t][0].call(o.exports, function (e) {
                    return u(s[t][1][e] || e)
                }, o, o.exports, i, s, a, l)
            }
            return a[t].exports
        }

        for (var c = "function" == typeof require && require, e = 0; e < l.length; e++) u(l[e]);
        return u
    }({
        1: [function (n, r, e) {
            (function (e) {
                "use strict";
                var t = n("./transport-list");
                r.exports = n("./main")(t), "_sockjs_onload" in e && setTimeout(e._sockjs_onload, 1)
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"./main": 14, "./transport-list": 16}],
        2: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./event");

            function i() {
                o.call(this), this.initEvent("close", !1, !1), this.wasClean = !1, this.code = 0, this.reason = ""
            }

            r(i, o), t.exports = i
        }, {"./event": 4, "inherits": 54}],
        3: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./eventtarget");

            function i() {
                o.call(this)
            }

            r(i, o), i.prototype.removeAllListeners = function (e) {
                e ? delete this._listeners[e] : this._listeners = {}
            }, i.prototype.once = function (t, n) {
                var r = this, o = !1;
                this.on(t, function e() {
                    r.removeListener(t, e), o || (o = !0, n.apply(this, arguments))
                })
            }, i.prototype.emit = function () {
                var e = arguments[0], t = this._listeners[e];
                if (t) {
                    for (var n = arguments.length, r = new Array(n - 1), o = 1; o < n; o++) r[o - 1] = arguments[o];
                    for (var i = 0; i < t.length; i++) t[i].apply(this, r)
                }
            }, i.prototype.on = i.prototype.addListener = o.prototype.addEventListener, i.prototype.removeListener = o.prototype.removeEventListener, t.exports.EventEmitter = i
        }, {"./eventtarget": 5, "inherits": 54}],
        4: [function (e, t, n) {
            "use strict";

            function r(e) {
                this.type = e
            }

            r.prototype.initEvent = function (e, t, n) {
                return this.type = e, this.bubbles = t, this.cancelable = n, this.timeStamp = +new Date, this
            }, r.prototype.stopPropagation = function () {
            }, r.prototype.preventDefault = function () {
            }, r.CAPTURING_PHASE = 1, r.AT_TARGET = 2, r.BUBBLING_PHASE = 3, t.exports = r
        }, {}],
        5: [function (e, t, n) {
            "use strict";

            function r() {
                this._listeners = {}
            }

            r.prototype.addEventListener = function (e, t) {
                e in this._listeners || (this._listeners[e] = []);
                var n = this._listeners[e];
                -1 === n.indexOf(t) && (n = n.concat([t])), this._listeners[e] = n
            }, r.prototype.removeEventListener = function (e, t) {
                var n = this._listeners[e];
                if (n) {
                    var r = n.indexOf(t);
                    -1 === r || (1 < n.length ? this._listeners[e] = n.slice(0, r).concat(n.slice(r + 1)) : delete this._listeners[e])
                }
            }, r.prototype.dispatchEvent = function () {
                var e = arguments[0], t = e.type, n = 1 === arguments.length ? [e] : Array.apply(null, arguments);
                if (this["on" + t] && this["on" + t].apply(this, n), t in this._listeners) for (var r = this._listeners[t], o = 0; o < r.length; o++) r[o].apply(this, n)
            }, t.exports = r
        }, {}],
        6: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./event");

            function i(e) {
                o.call(this), this.initEvent("message", !1, !1), this.data = e
            }

            r(i, o), t.exports = i
        }, {"./event": 4, "inherits": 54}],
        7: [function (e, t, n) {
            "use strict";
            var r = e("json3"), o = e("./utils/iframe");

            function i(e) {
                (this._transport = e).on("message", this._transportMessage.bind(this)), e.on("close", this._transportClose.bind(this))
            }

            i.prototype._transportClose = function (e, t) {
                o.postMessage("c", r.stringify([e, t]))
            }, i.prototype._transportMessage = function (e) {
                o.postMessage("t", e)
            }, i.prototype._send = function (e) {
                this._transport.send(e)
            }, i.prototype._close = function () {
                this._transport.close(), this._transport.removeAllListeners()
            }, t.exports = i
        }, {"./utils/iframe": 47, "json3": 55}],
        8: [function (e, t, n) {
            "use strict";
            var f = e("./utils/url"), r = e("./utils/event"), h = e("json3"), d = e("./facade"),
                o = e("./info-iframe-receiver"), p = e("./utils/iframe"), m = e("./location"), v = function () {
                };
            t.exports = function (l, e) {
                var u, c = {};
                e.forEach(function (e) {
                    e.facadeTransport && (c[e.facadeTransport.transportName] = e.facadeTransport)
                }), c[o.transportName] = o, l.bootstrap_iframe = function () {
                    var a;
                    p.currentWindowId = m.hash.slice(1);
                    r.attachEvent("message", function (t) {
                        if (t.source === parent && (void 0 === u && (u = t.origin), t.origin === u)) {
                            var n;
                            try {
                                n = h.parse(t.data)
                            } catch (e) {
                                return void v("bad json", t.data)
                            }
                            if (n.windowId === p.currentWindowId) switch (n.type) {
                                case"s":
                                    var e;
                                    try {
                                        e = h.parse(n.data)
                                    } catch (e) {
                                        v("bad json", n.data);
                                        break
                                    }
                                    var r = e[0], o = e[1], i = e[2], s = e[3];
                                    if (v(r, o, i, s), r !== l.version) throw new Error('Incompatible SockJS! Main site uses: "' + r + '", the iframe: "' + l.version + '".');
                                    if (!f.isOriginEqual(i, m.href) || !f.isOriginEqual(s, m.href)) throw new Error("Can't connect to different domain from within an iframe. (" + m.href + ", " + i + ", " + s + ")");
                                    a = new d(new c[o](i, s));
                                    break;
                                case"m":
                                    a._send(n.data);
                                    break;
                                case"c":
                                    a && a._close(), a = null
                            }
                        }
                    }), p.postMessage("s")
                }
            }
        }, {
            "./facade": 7,
            "./info-iframe-receiver": 10,
            "./location": 13,
            "./utils/event": 46,
            "./utils/iframe": 47,
            "./utils/url": 52,
            "debug": void 0,
            "json3": 55
        }],
        9: [function (e, t, n) {
            "use strict";
            var r = e("events").EventEmitter, o = e("inherits"), s = e("json3"), a = e("./utils/object"),
                l = function () {
                };

            function i(e, t) {
                r.call(this);
                var o = this, i = +new Date;
                this.xo = new t("GET", e), this.xo.once("finish", function (e, t) {
                    var n, r;
                    if (200 === e) {
                        if (r = +new Date - i, t) try {
                            n = s.parse(t)
                        } catch (e) {
                            l("bad json", t)
                        }
                        a.isObject(n) || (n = {})
                    }
                    o.emit("finish", n, r), o.removeAllListeners()
                })
            }

            o(i, r), i.prototype.close = function () {
                this.removeAllListeners(), this.xo.close()
            }, t.exports = i
        }, {"./utils/object": 49, "debug": void 0, "events": 3, "inherits": 54, "json3": 55}],
        10: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("events").EventEmitter, i = e("json3"), s = e("./transport/sender/xhr-local"),
                a = e("./info-ajax");

            function l(e) {
                var n = this;
                o.call(this), this.ir = new a(e, s), this.ir.once("finish", function (e, t) {
                    n.ir = null, n.emit("message", i.stringify([e, t]))
                })
            }

            r(l, o), l.transportName = "iframe-info-receiver", l.prototype.close = function () {
                this.ir && (this.ir.close(), this.ir = null), this.removeAllListeners()
            }, t.exports = l
        }, {"./info-ajax": 9, "./transport/sender/xhr-local": 37, "events": 3, "inherits": 54, "json3": 55}],
        11: [function (n, o, e) {
            (function (r) {
                "use strict";
                var i = n("events").EventEmitter, e = n("inherits"), s = n("json3"), a = n("./utils/event"),
                    l = n("./transport/iframe"), u = n("./info-iframe-receiver"), c = function () {
                    };

                function t(t, n) {
                    var o = this;
                    i.call(this);

                    function e() {
                        var e = o.ifr = new l(u.transportName, n, t);
                        e.once("message", function (t) {
                            if (t) {
                                var e;
                                try {
                                    e = s.parse(t)
                                } catch (e) {
                                    return c("bad json", t), o.emit("finish"), void o.close()
                                }
                                var n = e[0], r = e[1];
                                o.emit("finish", n, r)
                            }
                            o.close()
                        }), e.once("close", function () {
                            o.emit("finish"), o.close()
                        })
                    }

                    r.document.body ? e() : a.attachEvent("load", e)
                }

                e(t, i), t.enabled = function () {
                    return l.enabled()
                }, t.prototype.close = function () {
                    this.ifr && this.ifr.close(), this.removeAllListeners(), this.ifr = null
                }, o.exports = t
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {
            "./info-iframe-receiver": 10,
            "./transport/iframe": 22,
            "./utils/event": 46,
            "debug": void 0,
            "events": 3,
            "inherits": 54,
            "json3": 55
        }],
        12: [function (e, t, n) {
            "use strict";
            var r = e("events").EventEmitter, o = e("inherits"), i = e("./utils/url"), s = e("./transport/sender/xdr"),
                a = e("./transport/sender/xhr-cors"), l = e("./transport/sender/xhr-local"),
                u = e("./transport/sender/xhr-fake"), c = e("./info-iframe"), f = e("./info-ajax"), h = function () {
                };

            function d(e, t) {
                h(e);
                var n = this;
                r.call(this), setTimeout(function () {
                    n.doXhr(e, t)
                }, 0)
            }

            o(d, r), d._getReceiver = function (e, t, n) {
                return n.sameOrigin ? new f(t, l) : a.enabled ? new f(t, a) : s.enabled && n.sameScheme ? new f(t, s) : c.enabled() ? new c(e, t) : new f(t, u)
            }, d.prototype.doXhr = function (e, t) {
                var n = this, r = i.addPath(e, "/info");
                h("doXhr", r), this.xo = d._getReceiver(e, r, t), this.timeoutRef = setTimeout(function () {
                    h("timeout"), n._cleanup(!1), n.emit("finish")
                }, d.timeout), this.xo.once("finish", function (e, t) {
                    h("finish", e, t), n._cleanup(!0), n.emit("finish", e, t)
                })
            }, d.prototype._cleanup = function (e) {
                h("_cleanup"), clearTimeout(this.timeoutRef), this.timeoutRef = null, !e && this.xo && this.xo.close(), this.xo = null
            }, d.prototype.close = function () {
                h("close"), this.removeAllListeners(), this._cleanup(!1)
            }, d.timeout = 8e3, t.exports = d
        }, {
            "./info-ajax": 9,
            "./info-iframe": 11,
            "./transport/sender/xdr": 34,
            "./transport/sender/xhr-cors": 35,
            "./transport/sender/xhr-fake": 36,
            "./transport/sender/xhr-local": 37,
            "./utils/url": 52,
            "debug": void 0,
            "events": 3,
            "inherits": 54
        }],
        13: [function (e, t, n) {
            (function (e) {
                "use strict";
                t.exports = e.location || {
                    origin: "http://localhost:80",
                    protocol: "http:",
                    host: "localhost",
                    port: 80,
                    href: "http://localhost/",
                    hash: ""
                }
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        14: [function (_, E, e) {
            (function (i) {
                "use strict";
                _("./shims");
                var r, l = _("url-parse"), e = _("inherits"), s = _("json3"), u = _("./utils/random"),
                    t = _("./utils/escape"), c = _("./utils/url"), a = _("./utils/event"), n = _("./utils/transport"),
                    o = _("./utils/object"), f = _("./utils/browser"), h = _("./utils/log"), d = _("./event/event"),
                    p = _("./event/eventtarget"), m = _("./location"), v = _("./event/close"),
                    b = _("./event/trans-message"), y = _("./info-receiver"), g = function () {
                    };

                function w(e, t, n) {
                    if (!(this instanceof w)) return new w(e, t, n);
                    if (arguments.length < 1) throw new TypeError("Failed to construct 'SockJS: 1 argument required, but only 0 present");
                    p.call(this), this.readyState = w.CONNECTING, this.extensions = "", this.protocol = "", (n = n || {}).protocols_whitelist && h.warn("'protocols_whitelist' is DEPRECATED. Use 'transports' instead."), this._transportsWhitelist = n.transports, this._transportOptions = n.transportOptions || {}, this._timeout = n.timeout || 0;
                    var r = n.sessionId || 8;
                    if ("function" == typeof r) this._generateSessionId = r; else {
                        if ("number" != typeof r) throw new TypeError("If sessionId is used in the options, it needs to be a number or a function.");
                        this._generateSessionId = function () {
                            return u.string(r)
                        }
                    }
                    this._server = n.server || u.numberString(1e3);
                    var o = new l(e);
                    if (!o.host || !o.protocol) throw new SyntaxError("The URL '" + e + "' is invalid");
                    if (o.hash) throw new SyntaxError("The URL must not contain a fragment");
                    if ("http:" !== o.protocol && "https:" !== o.protocol) throw new SyntaxError("The URL's scheme must be either 'http:' or 'https:'. '" + o.protocol + "' is not allowed.");
                    var i = "https:" === o.protocol;
                    if ("https:" === m.protocol && !i && !c.isLoopbackAddr(o.hostname)) throw new Error("SecurityError: An insecure SockJS connection may not be initiated from a page loaded over HTTPS");
                    t ? Array.isArray(t) || (t = [t]) : t = [];
                    var s = t.sort();
                    s.forEach(function (e, t) {
                        if (!e) throw new SyntaxError("The protocols entry '" + e + "' is invalid.");
                        if (t < s.length - 1 && e === s[t + 1]) throw new SyntaxError("The protocols entry '" + e + "' is duplicated.")
                    });
                    var a = c.getOrigin(m.href);
                    this._origin = a ? a.toLowerCase() : null, o.set("pathname", o.pathname.replace(/\/+$/, "")), this.url = o.href, g("using url", this.url), this._urlInfo = {
                        nullOrigin: !f.hasDomain(),
                        sameOrigin: c.isOriginEqual(this.url, m.href),
                        sameScheme: c.isSchemeEqual(this.url, m.href)
                    }, this._ir = new y(this.url, this._urlInfo), this._ir.once("finish", this._receiveInfo.bind(this))
                }

                function x(e) {
                    return 1e3 === e || 3e3 <= e && e <= 4999
                }

                e(w, p), w.prototype.close = function (e, t) {
                    if (e && !x(e)) throw new Error("InvalidAccessError: Invalid code");
                    if (t && 123 < t.length) throw new SyntaxError("reason argument has an invalid length");
                    if (this.readyState !== w.CLOSING && this.readyState !== w.CLOSED) {
                        this._close(e || 1e3, t || "Normal closure", !0)
                    }
                }, w.prototype.send = function (e) {
                    if ("string" != typeof e && (e = "" + e), this.readyState === w.CONNECTING) throw new Error("InvalidStateError: The connection has not been established yet");
                    this.readyState === w.OPEN && this._transport.send(t.quote(e))
                }, w.version = _("./version"), w.CONNECTING = 0, w.OPEN = 1, w.CLOSING = 2, w.CLOSED = 3, w.prototype._receiveInfo = function (e, t) {
                    if (g("_receiveInfo", t), this._ir = null, e) {
                        this._rto = this.countRTO(t), this._transUrl = e.base_url ? e.base_url : this.url, e = o.extend(e, this._urlInfo), g("info", e);
                        var n = r.filterToEnabled(this._transportsWhitelist, e);
                        this._transports = n.main, g(this._transports.length + " enabled transports"), this._connect()
                    } else this._close(1002, "Cannot connect to server")
                }, w.prototype._connect = function () {
                    for (var e = this._transports.shift(); e; e = this._transports.shift()) {
                        if (g("attempt", e.transportName), e.needBody && (!i.document.body || void 0 !== i.document.readyState && "complete" !== i.document.readyState && "interactive" !== i.document.readyState)) return g("waiting for body"), this._transports.unshift(e), void a.attachEvent("load", this._connect.bind(this));
                        var t = Math.max(this._timeout, this._rto * e.roundTrips || 5e3);
                        this._transportTimeoutId = setTimeout(this._transportTimeout.bind(this), t), g("using timeout", t);
                        var n = c.addPath(this._transUrl, "/" + this._server + "/" + this._generateSessionId()),
                            r = this._transportOptions[e.transportName];
                        g("transport url", n);
                        var o = new e(n, this._transUrl, r);
                        return o.on("message", this._transportMessage.bind(this)), o.once("close", this._transportClose.bind(this)), o.transportName = e.transportName, void (this._transport = o)
                    }
                    this._close(2e3, "All transports failed", !1)
                }, w.prototype._transportTimeout = function () {
                    g("_transportTimeout"), this.readyState === w.CONNECTING && (this._transport && this._transport.close(), this._transportClose(2007, "Transport timed out"))
                }, w.prototype._transportMessage = function (e) {
                    g("_transportMessage", e);
                    var t, n = this, r = e.slice(0, 1), o = e.slice(1);
                    switch (r) {
                        case"o":
                            return void this._open();
                        case"h":
                            return this.dispatchEvent(new d("heartbeat")), void g("heartbeat", this.transport)
                    }
                    if (o) try {
                        t = s.parse(o)
                    } catch (e) {
                        g("bad json", o)
                    }
                    if (void 0 !== t) switch (r) {
                        case"a":
                            Array.isArray(t) && t.forEach(function (e) {
                                g("message", n.transport, e), n.dispatchEvent(new b(e))
                            });
                            break;
                        case"m":
                            g("message", this.transport, t), this.dispatchEvent(new b(t));
                            break;
                        case"c":
                            Array.isArray(t) && 2 === t.length && this._close(t[0], t[1], !0)
                    } else g("empty payload", o)
                }, w.prototype._transportClose = function (e, t) {
                    g("_transportClose", this.transport, e, t), this._transport && (this._transport.removeAllListeners(), this._transport = null, this.transport = null), x(e) || 2e3 === e || this.readyState !== w.CONNECTING ? this._close(e, t) : this._connect()
                }, w.prototype._open = function () {
                    g("_open", this._transport && this._transport.transportName, this.readyState), this.readyState === w.CONNECTING ? (this._transportTimeoutId && (clearTimeout(this._transportTimeoutId), this._transportTimeoutId = null), this.readyState = w.OPEN, this.transport = this._transport.transportName, this.dispatchEvent(new d("open")), g("connected", this.transport)) : this._close(1006, "Server lost session")
                }, w.prototype._close = function (t, n, r) {
                    g("_close", this.transport, t, n, r, this.readyState);
                    var o = !1;
                    if (this._ir && (o = !0, this._ir.close(), this._ir = null), this._transport && (this._transport.close(), this._transport = null, this.transport = null), this.readyState === w.CLOSED) throw new Error("InvalidStateError: SockJS has already been closed");
                    this.readyState = w.CLOSING, setTimeout(function () {
                        this.readyState = w.CLOSED, o && this.dispatchEvent(new d("error"));
                        var e = new v("close");
                        e.wasClean = r || !1, e.code = t || 1e3, e.reason = n, this.dispatchEvent(e), this.onmessage = this.onclose = this.onerror = null, g("disconnected")
                    }.bind(this), 0)
                }, w.prototype.countRTO = function (e) {
                    return 100 < e ? 4 * e : 300 + e
                }, E.exports = function (e) {
                    return r = n(e), _("./iframe-bootstrap")(w, e), w
                }
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {
            "./event/close": 2,
            "./event/event": 4,
            "./event/eventtarget": 5,
            "./event/trans-message": 6,
            "./iframe-bootstrap": 8,
            "./info-receiver": 12,
            "./location": 13,
            "./shims": 15,
            "./utils/browser": 44,
            "./utils/escape": 45,
            "./utils/event": 46,
            "./utils/log": 48,
            "./utils/object": 49,
            "./utils/random": 50,
            "./utils/transport": 51,
            "./utils/url": 52,
            "./version": 53,
            "debug": void 0,
            "inherits": 54,
            "json3": 55,
            "url-parse": 58
        }],
        15: [function (e, t, n) {
            "use strict";

            function a(e) {
                return "[object Function]" === i.toString.call(e)
            }

            function l(e) {
                return "[object String]" === f.call(e)
            }

            var o, c = Array.prototype, i = Object.prototype, r = Function.prototype, s = String.prototype, u = c.slice,
                f = i.toString, h = Object.defineProperty && function () {
                    try {
                        return Object.defineProperty({}, "x", {}), !0
                    } catch (e) {
                        return !1
                    }
                }();
            o = h ? function (e, t, n, r) {
                !r && t in e || Object.defineProperty(e, t, {configurable: !0, enumerable: !1, writable: !0, value: n})
            } : function (e, t, n, r) {
                !r && t in e || (e[t] = n)
            };

            function d(e, t, n) {
                for (var r in t) i.hasOwnProperty.call(t, r) && o(e, r, t[r], n)
            }

            function p(e) {
                if (null == e) throw new TypeError("can't convert " + e + " to object");
                return Object(e)
            }

            function m() {
            }

            d(r, {
                bind: function (t) {
                    var n = this;
                    if (!a(n)) throw new TypeError("Function.prototype.bind called on incompatible " + n);
                    for (var r = u.call(arguments, 1), e = Math.max(0, n.length - r.length), o = [], i = 0; i < e; i++) o.push("$" + i);
                    var s = Function("binder", "return function (" + o.join(",") + "){ return binder.apply(this, arguments); }")(function () {
                        if (this instanceof s) {
                            var e = n.apply(this, r.concat(u.call(arguments)));
                            return Object(e) === e ? e : this
                        }
                        return n.apply(t, r.concat(u.call(arguments)))
                    });
                    return n.prototype && (m.prototype = n.prototype, s.prototype = new m, m.prototype = null), s
                }
            }), d(Array, {
                isArray: function (e) {
                    return "[object Array]" === f.call(e)
                }
            });
            var v, b, y, g = Object("a"), w = "a" !== g[0] || !(0 in g);
            d(c, {
                forEach: function (e, t) {
                    var n = p(this), r = w && l(this) ? this.split("") : n, o = t, i = -1, s = r.length >>> 0;
                    if (!a(e)) throw new TypeError;
                    for (; ++i < s;) i in r && e.call(o, r[i], i, n)
                }
            }, (v = c.forEach, y = b = !0, v && (v.call("foo", function (e, t, n) {
                "object" != typeof n && (b = !1)
            }), v.call([1], function () {
                y = "string" == typeof this
            }, "x")), !(v && b && y)));
            var x = Array.prototype.indexOf && -1 !== [0, 1].indexOf(1, 2);
            d(c, {
                indexOf: function (e, t) {
                    var n = w && l(this) ? this.split("") : p(this), r = n.length >>> 0;
                    if (!r) return -1;
                    var o = 0;
                    for (1 < arguments.length && (o = function (e) {
                        var t = +e;
                        return t != t ? t = 0 : 0 !== t && t !== 1 / 0 && t !== -1 / 0 && (t = (0 < t || -1) * Math.floor(Math.abs(t))), t
                    }(t)), o = 0 <= o ? o : Math.max(0, r + o); o < r; o++) if (o in n && n[o] === e) return o;
                    return -1
                }
            }, x);
            var _, E = s.split;
            2 !== "ab".split(/(?:ab)*/).length || 4 !== ".".split(/(.?)(.?)/).length || "t" === "tesst".split(/(s)*/)[1] || 4 !== "test".split(/(?:)/, -1).length || "".split(/.?/).length || 1 < ".".split(/()()/).length ? (_ = void 0 === /()??/.exec("")[1], s.split = function (e, t) {
                var n = this;
                if (void 0 === e && 0 === t) return [];
                if ("[object RegExp]" !== f.call(e)) return E.call(this, e, t);
                var r, o, i, s, a = [],
                    l = (e.ignoreCase ? "i" : "") + (e.multiline ? "m" : "") + (e.extended ? "x" : "") + (e.sticky ? "y" : ""),
                    u = 0;
                for (e = new RegExp(e.source, l + "g"), n += "", _ || (r = new RegExp("^" + e.source + "$(?!\\s)", l)), t = void 0 === t ? -1 >>> 0 : function (e) {
                    return e >>> 0
                }(t); (o = e.exec(n)) && !(u < (i = o.index + o[0].length) && (a.push(n.slice(u, o.index)), !_ && 1 < o.length && o[0].replace(r, function () {
                    for (var e = 1; e < arguments.length - 2; e++) void 0 === arguments[e] && (o[e] = void 0)
                }), 1 < o.length && o.index < n.length && c.push.apply(a, o.slice(1)), s = o[0].length, u = i, a.length >= t));) e.lastIndex === o.index && e.lastIndex++;
                return u === n.length ? !s && e.test("") || a.push("") : a.push(n.slice(u)), a.length > t ? a.slice(0, t) : a
            }) : "0".split(void 0, 0).length && (s.split = function (e, t) {
                return void 0 === e && 0 === t ? [] : E.call(this, e, t)
            });
            var j = s.substr, S = "".substr && "b" !== "0b".substr(-1);
            d(s, {
                substr: function (e, t) {
                    return j.call(this, e < 0 && (e = this.length + e) < 0 ? 0 : e, t)
                }
            }, S)
        }, {}],
        16: [function (e, t, n) {
            "use strict";
            t.exports = [e("./transport/websocket"), e("./transport/xhr-streaming"), e("./transport/xdr-streaming"), e("./transport/eventsource"), e("./transport/lib/iframe-wrap")(e("./transport/eventsource")), e("./transport/htmlfile"), e("./transport/lib/iframe-wrap")(e("./transport/htmlfile")), e("./transport/xhr-polling"), e("./transport/xdr-polling"), e("./transport/lib/iframe-wrap")(e("./transport/xhr-polling")), e("./transport/jsonp-polling")]
        }, {
            "./transport/eventsource": 20,
            "./transport/htmlfile": 21,
            "./transport/jsonp-polling": 23,
            "./transport/lib/iframe-wrap": 26,
            "./transport/websocket": 38,
            "./transport/xdr-polling": 39,
            "./transport/xdr-streaming": 40,
            "./transport/xhr-polling": 41,
            "./transport/xhr-streaming": 42
        }],
        17: [function (o, f, e) {
            (function (e) {
                "use strict";
                var i = o("events").EventEmitter, t = o("inherits"), s = o("../../utils/event"),
                    a = o("../../utils/url"), l = e.XMLHttpRequest, u = function () {
                    };

                function c(e, t, n, r) {
                    u(e, t);
                    var o = this;
                    i.call(this), setTimeout(function () {
                        o._start(e, t, n, r)
                    }, 0)
                }

                t(c, i), c.prototype._start = function (e, t, n, r) {
                    var o = this;
                    try {
                        this.xhr = new l
                    } catch (e) {
                    }
                    if (!this.xhr) return u("no xhr"), this.emit("finish", 0, "no xhr support"), void this._cleanup();
                    t = a.addQuery(t, "t=" + +new Date), this.unloadRef = s.unloadAdd(function () {
                        u("unload cleanup"), o._cleanup(!0)
                    });
                    try {
                        this.xhr.open(e, t, !0), this.timeout && "timeout" in this.xhr && (this.xhr.timeout = this.timeout, this.xhr.ontimeout = function () {
                            u("xhr timeout"), o.emit("finish", 0, ""), o._cleanup(!1)
                        })
                    } catch (e) {
                        return u("exception", e), this.emit("finish", 0, ""), void this._cleanup(!1)
                    }
                    if (r && r.noCredentials || !c.supportsCORS || (u("withCredentials"), this.xhr.withCredentials = !0), r && r.headers) for (var i in r.headers) this.xhr.setRequestHeader(i, r.headers[i]);
                    this.xhr.onreadystatechange = function () {
                        if (o.xhr) {
                            var e, t, n = o.xhr;
                            switch (u("readyState", n.readyState), n.readyState) {
                                case 3:
                                    try {
                                        t = n.status, e = n.responseText
                                    } catch (e) {
                                    }
                                    u("status", t), 1223 === t && (t = 204), 200 === t && e && 0 < e.length && (u("chunk"), o.emit("chunk", t, e));
                                    break;
                                case 4:
                                    t = n.status, u("status", t), 1223 === t && (t = 204), 12005 !== t && 12029 !== t || (t = 0), u("finish", t, n.responseText), o.emit("finish", t, n.responseText), o._cleanup(!1)
                            }
                        }
                    };
                    try {
                        o.xhr.send(n)
                    } catch (e) {
                        o.emit("finish", 0, ""), o._cleanup(!1)
                    }
                }, c.prototype._cleanup = function (e) {
                    if (u("cleanup"), this.xhr) {
                        if (this.removeAllListeners(), s.unloadDel(this.unloadRef), this.xhr.onreadystatechange = function () {
                        }, this.xhr.ontimeout && (this.xhr.ontimeout = null), e) try {
                            this.xhr.abort()
                        } catch (e) {
                        }
                        this.unloadRef = this.xhr = null
                    }
                }, c.prototype.close = function () {
                    u("close"), this._cleanup(!0)
                }, c.enabled = !!l;
                var n = ["Active"].concat("Object").join("X");
                !c.enabled && n in e && (u("overriding xmlhttprequest"), c.enabled = !!new (l = function () {
                    try {
                        return new e[n]("Microsoft.XMLHTTP")
                    } catch (e) {
                        return null
                    }
                }));
                var r = !1;
                try {
                    r = "withCredentials" in new l
                } catch (e) {
                }
                c.supportsCORS = r, f.exports = c
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"../../utils/event": 46, "../../utils/url": 52, "debug": void 0, "events": 3, "inherits": 54}],
        18: [function (e, t, n) {
            (function (e) {
                t.exports = e.EventSource
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        19: [function (e, n, t) {
            (function (e) {
                "use strict";
                var t = e.WebSocket || e.MozWebSocket;
                n.exports = t ? function (e) {
                    return new t(e)
                } : void 0
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        20: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./lib/ajax-based"), i = e("./receiver/eventsource"),
                s = e("./sender/xhr-cors"), a = e("eventsource");

            function l(e) {
                if (!l.enabled()) throw new Error("Transport created when disabled");
                o.call(this, e, "/eventsource", i, s)
            }

            r(l, o), l.enabled = function () {
                return !!a
            }, l.transportName = "eventsource", l.roundTrips = 2, t.exports = l
        }, {
            "./lib/ajax-based": 24,
            "./receiver/eventsource": 29,
            "./sender/xhr-cors": 35,
            "eventsource": 18,
            "inherits": 54
        }],
        21: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./receiver/htmlfile"), i = e("./sender/xhr-local"), s = e("./lib/ajax-based");

            function a(e) {
                if (!o.enabled) throw new Error("Transport created when disabled");
                s.call(this, e, "/htmlfile", o, i)
            }

            r(a, s), a.enabled = function (e) {
                return o.enabled && e.sameOrigin
            }, a.transportName = "htmlfile", a.roundTrips = 2, t.exports = a
        }, {"./lib/ajax-based": 24, "./receiver/htmlfile": 30, "./sender/xhr-local": 37, "inherits": 54}],
        22: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("json3"), i = e("events").EventEmitter, s = e("../version"),
                a = e("../utils/url"), l = e("../utils/iframe"), u = e("../utils/event"), c = e("../utils/random"),
                f = function () {
                };

            function h(e, t, n) {
                if (!h.enabled()) throw new Error("Transport created when disabled");
                i.call(this);
                var r = this;
                this.origin = a.getOrigin(n), this.baseUrl = n, this.transUrl = t, this.transport = e, this.windowId = c.string(8);
                var o = a.addPath(n, "/iframe.html") + "#" + this.windowId;
                f(e, t, o), this.iframeObj = l.createIframe(o, function (e) {
                    f("err callback"), r.emit("close", 1006, "Unable to load an iframe (" + e + ")"), r.close()
                }), this.onmessageCallback = this._message.bind(this), u.attachEvent("message", this.onmessageCallback)
            }

            r(h, i), h.prototype.close = function () {
                if (f("close"), this.removeAllListeners(), this.iframeObj) {
                    u.detachEvent("message", this.onmessageCallback);
                    try {
                        this.postMessage("c")
                    } catch (e) {
                    }
                    this.iframeObj.cleanup(), this.iframeObj = null, this.onmessageCallback = this.iframeObj = null
                }
            }, h.prototype._message = function (t) {
                if (f("message", t.data), a.isOriginEqual(t.origin, this.origin)) {
                    var n;
                    try {
                        n = o.parse(t.data)
                    } catch (e) {
                        return void f("bad json", t.data)
                    }
                    if (n.windowId === this.windowId) switch (n.type) {
                        case"s":
                            this.iframeObj.loaded(), this.postMessage("s", o.stringify([s, this.transport, this.transUrl, this.baseUrl]));
                            break;
                        case"t":
                            this.emit("message", n.data);
                            break;
                        case"c":
                            var e;
                            try {
                                e = o.parse(n.data)
                            } catch (e) {
                                return void f("bad json", n.data)
                            }
                            this.emit("close", e[0], e[1]), this.close()
                    } else f("mismatched window id", n.windowId, this.windowId)
                } else f("not same origin", t.origin, this.origin)
            }, h.prototype.postMessage = function (e, t) {
                f("postMessage", e, t), this.iframeObj.post(o.stringify({
                    windowId: this.windowId,
                    type: e,
                    data: t || ""
                }), this.origin)
            }, h.prototype.send = function (e) {
                f("send", e), this.postMessage("m", e)
            }, h.enabled = function () {
                return l.iframeEnabled
            }, h.transportName = "iframe", h.roundTrips = 2, t.exports = h
        }, {
            "../utils/event": 46,
            "../utils/iframe": 47,
            "../utils/random": 50,
            "../utils/url": 52,
            "../version": 53,
            "debug": void 0,
            "events": 3,
            "inherits": 54,
            "json3": 55
        }],
        23: [function (s, a, e) {
            (function (e) {
                "use strict";
                var t = s("inherits"), n = s("./lib/sender-receiver"), r = s("./receiver/jsonp"),
                    o = s("./sender/jsonp");

                function i(e) {
                    if (!i.enabled()) throw new Error("Transport created when disabled");
                    n.call(this, e, "/jsonp", o, r)
                }

                t(i, n), i.enabled = function () {
                    return !!e.document
                }, i.transportName = "jsonp-polling", i.roundTrips = 1, i.needBody = !0, a.exports = i
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"./lib/sender-receiver": 28, "./receiver/jsonp": 31, "./sender/jsonp": 33, "inherits": 54}],
        24: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), a = e("../../utils/url"), o = e("./sender-receiver"), l = function () {
            };

            function i(e, t, n, r) {
                o.call(this, e, t, function (s) {
                    return function (e, t, n) {
                        l("create ajax sender", e, t);
                        var r = {};
                        "string" == typeof t && (r.headers = {"Content-type": "text/plain"});
                        var o = a.addPath(e, "/xhr_send"), i = new s("POST", o, t, r);
                        return i.once("finish", function (e) {
                            if (l("finish", e), i = null, 200 !== e && 204 !== e) return n(new Error("http status " + e));
                            n()
                        }), function () {
                            l("abort"), i.close(), i = null;
                            var e = new Error("Aborted");
                            e.code = 1e3, n(e)
                        }
                    }
                }(r), n, r)
            }

            r(i, o), t.exports = i
        }, {"../../utils/url": 52, "./sender-receiver": 28, "debug": void 0, "inherits": 54}],
        25: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("events").EventEmitter, i = function () {
            };

            function s(e, t) {
                i(e), o.call(this), this.sendBuffer = [], this.sender = t, this.url = e
            }

            r(s, o), s.prototype.send = function (e) {
                i("send", e), this.sendBuffer.push(e), this.sendStop || this.sendSchedule()
            }, s.prototype.sendScheduleWait = function () {
                i("sendScheduleWait");
                var e, t = this;
                this.sendStop = function () {
                    i("sendStop"), t.sendStop = null, clearTimeout(e)
                }, e = setTimeout(function () {
                    i("timeout"), t.sendStop = null, t.sendSchedule()
                }, 25)
            }, s.prototype.sendSchedule = function () {
                i("sendSchedule", this.sendBuffer.length);
                var t = this;
                if (0 < this.sendBuffer.length) {
                    var e = "[" + this.sendBuffer.join(",") + "]";
                    this.sendStop = this.sender(this.url, e, function (e) {
                        t.sendStop = null, e ? (i("error", e), t.emit("close", e.code || 1006, "Sending error: " + e), t.close()) : t.sendScheduleWait()
                    }), this.sendBuffer = []
                }
            }, s.prototype._cleanup = function () {
                i("_cleanup"), this.removeAllListeners()
            }, s.prototype.close = function () {
                i("close"), this._cleanup(), this.sendStop && (this.sendStop(), this.sendStop = null)
            }, t.exports = s
        }, {"debug": void 0, "events": 3, "inherits": 54}],
        26: [function (e, n, t) {
            (function (o) {
                "use strict";
                var t = e("inherits"), i = e("../iframe"), s = e("../../utils/object");
                n.exports = function (r) {
                    function e(e, t) {
                        i.call(this, r.transportName, e, t)
                    }

                    return t(e, i), e.enabled = function (e, t) {
                        if (!o.document) return !1;
                        var n = s.extend({}, t);
                        return n.sameOrigin = !0, r.enabled(n) && i.enabled()
                    }, e.transportName = "iframe-" + r.transportName, e.needBody = !0, e.roundTrips = i.roundTrips + r.roundTrips - 1, e.facadeTransport = r, e
                }
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"../../utils/object": 49, "../iframe": 22, "inherits": 54}],
        27: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("events").EventEmitter, i = function () {
            };

            function s(e, t, n) {
                i(t), o.call(this), this.Receiver = e, this.receiveUrl = t, this.AjaxObject = n, this._scheduleReceiver()
            }

            r(s, o), s.prototype._scheduleReceiver = function () {
                i("_scheduleReceiver");
                var n = this, r = this.poll = new this.Receiver(this.receiveUrl, this.AjaxObject);
                r.on("message", function (e) {
                    i("message", e), n.emit("message", e)
                }), r.once("close", function (e, t) {
                    i("close", e, t, n.pollIsClosing), n.poll = r = null, n.pollIsClosing || ("network" === t ? n._scheduleReceiver() : (n.emit("close", e || 1006, t), n.removeAllListeners()))
                })
            }, s.prototype.abort = function () {
                i("abort"), this.removeAllListeners(), this.pollIsClosing = !0, this.poll && this.poll.abort()
            }, t.exports = s
        }, {"debug": void 0, "events": 3, "inherits": 54}],
        28: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), a = e("../../utils/url"), l = e("./buffered-sender"), u = e("./polling"),
                c = function () {
                };

            function o(e, t, n, r, o) {
                var i = a.addPath(e, t);
                c(i);
                var s = this;
                l.call(this, e, n), this.poll = new u(r, i, o), this.poll.on("message", function (e) {
                    c("poll message", e), s.emit("message", e)
                }), this.poll.once("close", function (e, t) {
                    c("poll close", e, t), s.poll = null, s.emit("close", e, t), s.close()
                })
            }

            r(o, l), o.prototype.close = function () {
                l.prototype.close.call(this), c("close"), this.removeAllListeners(), this.poll && (this.poll.abort(), this.poll = null)
            }, t.exports = o
        }, {"../../utils/url": 52, "./buffered-sender": 25, "./polling": 27, "debug": void 0, "inherits": 54}],
        29: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("events").EventEmitter, i = e("eventsource"), s = function () {
            };

            function a(e) {
                s(e), o.call(this);
                var n = this, r = this.es = new i(e);
                r.onmessage = function (e) {
                    s("message", e.data), n.emit("message", decodeURI(e.data))
                }, r.onerror = function (e) {
                    s("error", r.readyState, e);
                    var t = 2 !== r.readyState ? "network" : "permanent";
                    n._cleanup(), n._close(t)
                }
            }

            r(a, o), a.prototype.abort = function () {
                s("abort"), this._cleanup(), this._close("user")
            }, a.prototype._cleanup = function () {
                s("cleanup");
                var e = this.es;
                e && (e.onmessage = e.onerror = null, e.close(), this.es = null)
            }, a.prototype._close = function (e) {
                s("close", e);
                var t = this;
                setTimeout(function () {
                    t.emit("close", null, e), t.removeAllListeners()
                }, 200)
            }, t.exports = a
        }, {"debug": void 0, "events": 3, "eventsource": 18, "inherits": 54}],
        30: [function (n, c, e) {
            (function (r) {
                "use strict";
                var e = n("inherits"), o = n("../../utils/iframe"), i = n("../../utils/url"),
                    s = n("events").EventEmitter, a = n("../../utils/random"), l = function () {
                    };

                function u(e) {
                    l(e), s.call(this);
                    var t = this;
                    o.polluteGlobalNamespace(), this.id = "a" + a.string(6), e = i.addQuery(e, "c=" + decodeURIComponent(o.WPrefix + "." + this.id)), l("using htmlfile", u.htmlfileEnabled);
                    var n = u.htmlfileEnabled ? o.createHtmlfile : o.createIframe;
                    r[o.WPrefix][this.id] = {
                        start: function () {
                            l("start"), t.iframeObj.loaded()
                        }, message: function (e) {
                            l("message", e), t.emit("message", e)
                        }, stop: function () {
                            l("stop"), t._cleanup(), t._close("network")
                        }
                    }, this.iframeObj = n(e, function () {
                        l("callback"), t._cleanup(), t._close("permanent")
                    })
                }

                e(u, s), u.prototype.abort = function () {
                    l("abort"), this._cleanup(), this._close("user")
                }, u.prototype._cleanup = function () {
                    l("_cleanup"), this.iframeObj && (this.iframeObj.cleanup(), this.iframeObj = null), delete r[o.WPrefix][this.id]
                }, u.prototype._close = function (e) {
                    l("_close", e), this.emit("close", null, e), this.removeAllListeners()
                }, u.htmlfileEnabled = !1;
                var t = ["Active"].concat("Object").join("X");
                if (t in r) try {
                    u.htmlfileEnabled = !!new r[t]("htmlfile")
                } catch (e) {
                }
                u.enabled = u.htmlfileEnabled || o.iframeEnabled, c.exports = u
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {
            "../../utils/iframe": 47,
            "../../utils/random": 50,
            "../../utils/url": 52,
            "debug": void 0,
            "events": 3,
            "inherits": 54
        }],
        31: [function (t, n, e) {
            (function (i) {
                "use strict";
                var r = t("../../utils/iframe"), s = t("../../utils/random"), a = t("../../utils/browser"),
                    o = t("../../utils/url"), e = t("inherits"), l = t("events").EventEmitter, u = function () {
                    };

                function c(e) {
                    u(e);
                    var t = this;
                    l.call(this), r.polluteGlobalNamespace(), this.id = "a" + s.string(6);
                    var n = o.addQuery(e, "c=" + encodeURIComponent(r.WPrefix + "." + this.id));
                    i[r.WPrefix][this.id] = this._callback.bind(this), this._createScript(n), this.timeoutId = setTimeout(function () {
                        u("timeout"), t._abort(new Error("JSONP script loaded abnormally (timeout)"))
                    }, c.timeout)
                }

                e(c, l), c.prototype.abort = function () {
                    if (u("abort"), i[r.WPrefix][this.id]) {
                        var e = new Error("JSONP user aborted read");
                        e.code = 1e3, this._abort(e)
                    }
                }, c.timeout = 35e3, c.scriptErrorTimeout = 1e3, c.prototype._callback = function (e) {
                    u("_callback", e), this._cleanup(), this.aborting || (e && (u("message", e), this.emit("message", e)), this.emit("close", null, "network"), this.removeAllListeners())
                }, c.prototype._abort = function (e) {
                    u("_abort", e), this._cleanup(), this.aborting = !0, this.emit("close", e.code, e.message), this.removeAllListeners()
                }, c.prototype._cleanup = function () {
                    if (u("_cleanup"), clearTimeout(this.timeoutId), this.script2 && (this.script2.parentNode.removeChild(this.script2), this.script2 = null), this.script) {
                        var e = this.script;
                        e.parentNode.removeChild(e), e.onreadystatechange = e.onerror = e.onload = e.onclick = null, this.script = null
                    }
                    delete i[r.WPrefix][this.id]
                }, c.prototype._scriptError = function () {
                    u("_scriptError");
                    var e = this;
                    this.errorTimer || (this.errorTimer = setTimeout(function () {
                        e.loadedOkay || e._abort(new Error("JSONP script loaded abnormally (onerror)"))
                    }, c.scriptErrorTimeout))
                }, c.prototype._createScript = function (e) {
                    u("_createScript", e);
                    var t, n = this, r = this.script = i.document.createElement("script");
                    if (r.id = "a" + s.string(8), r.src = e, r.type = "text/javascript", r.charset = "UTF-8", r.onerror = this._scriptError.bind(this), r.onload = function () {
                        u("onload"), n._abort(new Error("JSONP script loaded abnormally (onload)"))
                    }, r.onreadystatechange = function () {
                        if (u("onreadystatechange", r.readyState), /loaded|closed/.test(r.readyState)) {
                            if (r && r.htmlFor && r.onclick) {
                                n.loadedOkay = !0;
                                try {
                                    r.onclick()
                                } catch (e) {
                                }
                            }
                            r && n._abort(new Error("JSONP script loaded abnormally (onreadystatechange)"))
                        }
                    }, void 0 === r.async && i.document.attachEvent) if (a.isOpera()) (t = this.script2 = i.document.createElement("script")).text = "try{var a = document.getElementById('" + r.id + "'); if(a)a.onerror();}catch(x){};", r.async = t.async = !1; else {
                        try {
                            r.htmlFor = r.id, r.event = "onclick"
                        } catch (e) {
                        }
                        r.async = !0
                    }
                    void 0 !== r.async && (r.async = !0);
                    var o = i.document.getElementsByTagName("head")[0];
                    o.insertBefore(r, o.firstChild), t && o.insertBefore(t, o.firstChild)
                }, n.exports = c
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {
            "../../utils/browser": 44,
            "../../utils/iframe": 47,
            "../../utils/random": 50,
            "../../utils/url": 52,
            "debug": void 0,
            "events": 3,
            "inherits": 54
        }],
        32: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("events").EventEmitter, i = function () {
            };

            function s(e, t) {
                i(e), o.call(this);
                var r = this;
                this.bufferPosition = 0, this.xo = new t("POST", e, null), this.xo.on("chunk", this._chunkHandler.bind(this)), this.xo.once("finish", function (e, t) {
                    i("finish", e, t), r._chunkHandler(e, t), r.xo = null;
                    var n = 200 === e ? "network" : "permanent";
                    i("close", n), r.emit("close", null, n), r._cleanup()
                })
            }

            r(s, o), s.prototype._chunkHandler = function (e, t) {
                if (i("_chunkHandler", e), 200 === e && t) for (var n = -1; ; this.bufferPosition += n + 1) {
                    var r = t.slice(this.bufferPosition);
                    if (-1 === (n = r.indexOf("\n"))) break;
                    var o = r.slice(0, n);
                    o && (i("message", o), this.emit("message", o))
                }
            }, s.prototype._cleanup = function () {
                i("_cleanup"), this.removeAllListeners()
            }, s.prototype.abort = function () {
                i("abort"), this.xo && (this.xo.close(), i("close"), this.emit("close", null, "user"), this.xo = null), this._cleanup()
            }, t.exports = s
        }, {"debug": void 0, "events": 3, "inherits": 54}],
        33: [function (e, t, n) {
            (function (s) {
                "use strict";
                var a, l, u = e("../../utils/random"), c = e("../../utils/url"), f = function () {
                };
                t.exports = function (e, t, n) {
                    f(e, t), a || (f("createForm"), (a = s.document.createElement("form")).style.display = "none", a.style.position = "absolute", a.method = "POST", a.enctype = "application/x-www-form-urlencoded", a.acceptCharset = "UTF-8", (l = s.document.createElement("textarea")).name = "d", a.appendChild(l), s.document.body.appendChild(a));
                    var r = "a" + u.string(8);
                    a.target = r, a.action = c.addQuery(c.addPath(e, "/jsonp_send"), "i=" + r);
                    var o = function (t) {
                        f("createIframe", t);
                        try {
                            return s.document.createElement('<iframe name="' + t + '">')
                        } catch (e) {
                            var n = s.document.createElement("iframe");
                            return n.name = t, n
                        }
                    }(r);
                    o.id = r, o.style.display = "none", a.appendChild(o);
                    try {
                        l.value = t
                    } catch (e) {
                    }
                    a.submit();

                    function i(e) {
                        f("completed", r, e), o.onerror && (o.onreadystatechange = o.onerror = o.onload = null, setTimeout(function () {
                            f("cleaning up", r), o.parentNode.removeChild(o), o = null
                        }, 500), l.value = "", n(e))
                    }

                    return o.onerror = function () {
                        f("onerror", r), i()
                    }, o.onload = function () {
                        f("onload", r), i()
                    }, o.onreadystatechange = function (e) {
                        f("onreadystatechange", r, o.readyState, e), "complete" === o.readyState && i()
                    }, function () {
                        f("aborted", r), i(new Error("Aborted"))
                    }
                }
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"../../utils/random": 50, "../../utils/url": 52, "debug": void 0}],
        34: [function (r, u, e) {
            (function (i) {
                "use strict";
                var o = r("events").EventEmitter, e = r("inherits"), s = r("../../utils/event"),
                    t = r("../../utils/browser"), a = r("../../utils/url"), l = function () {
                    };

                function n(e, t, n) {
                    l(e, t);
                    var r = this;
                    o.call(this), setTimeout(function () {
                        r._start(e, t, n)
                    }, 0)
                }

                e(n, o), n.prototype._start = function (e, t, n) {
                    l("_start");
                    var r = this, o = new i.XDomainRequest;
                    t = a.addQuery(t, "t=" + +new Date), o.onerror = function () {
                        l("onerror"), r._error()
                    }, o.ontimeout = function () {
                        l("ontimeout"), r._error()
                    }, o.onprogress = function () {
                        l("progress", o.responseText), r.emit("chunk", 200, o.responseText)
                    }, o.onload = function () {
                        l("load"), r.emit("finish", 200, o.responseText), r._cleanup(!1)
                    }, this.xdr = o, this.unloadRef = s.unloadAdd(function () {
                        r._cleanup(!0)
                    });
                    try {
                        this.xdr.open(e, t), this.timeout && (this.xdr.timeout = this.timeout), this.xdr.send(n)
                    } catch (e) {
                        this._error()
                    }
                }, n.prototype._error = function () {
                    this.emit("finish", 0, ""), this._cleanup(!1)
                }, n.prototype._cleanup = function (e) {
                    if (l("cleanup", e), this.xdr) {
                        if (this.removeAllListeners(), s.unloadDel(this.unloadRef), this.xdr.ontimeout = this.xdr.onerror = this.xdr.onprogress = this.xdr.onload = null, e) try {
                            this.xdr.abort()
                        } catch (e) {
                        }
                        this.unloadRef = this.xdr = null
                    }
                }, n.prototype.close = function () {
                    l("close"), this._cleanup(!0)
                }, n.enabled = !(!i.XDomainRequest || !t.hasDomain()), u.exports = n
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {
            "../../utils/browser": 44,
            "../../utils/event": 46,
            "../../utils/url": 52,
            "debug": void 0,
            "events": 3,
            "inherits": 54
        }],
        35: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("../driver/xhr");

            function i(e, t, n, r) {
                o.call(this, e, t, n, r)
            }

            r(i, o), i.enabled = o.enabled && o.supportsCORS, t.exports = i
        }, {"../driver/xhr": 17, "inherits": 54}],
        36: [function (e, t, n) {
            "use strict";
            var r = e("events").EventEmitter;

            function o() {
                var e = this;
                r.call(this), this.to = setTimeout(function () {
                    e.emit("finish", 200, "{}")
                }, o.timeout)
            }

            e("inherits")(o, r), o.prototype.close = function () {
                clearTimeout(this.to)
            }, o.timeout = 2e3, t.exports = o
        }, {"events": 3, "inherits": 54}],
        37: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("../driver/xhr");

            function i(e, t, n) {
                o.call(this, e, t, n, {noCredentials: !0})
            }

            r(i, o), i.enabled = o.enabled, t.exports = i
        }, {"../driver/xhr": 17, "inherits": 54}],
        38: [function (e, t, n) {
            "use strict";
            var i = e("../utils/event"), s = e("../utils/url"), r = e("inherits"), a = e("events").EventEmitter,
                l = e("./driver/websocket"), u = function () {
                };

            function c(e, t, n) {
                if (!c.enabled()) throw new Error("Transport created when disabled");
                a.call(this), u("constructor", e);
                var r = this, o = s.addPath(e, "/websocket");
                o = "https" === o.slice(0, 5) ? "wss" + o.slice(5) : "ws" + o.slice(4), this.url = o, this.ws = new l(this.url, [], n), this.ws.onmessage = function (e) {
                    u("message event", e.data), r.emit("message", e.data)
                }, this.unloadRef = i.unloadAdd(function () {
                    u("unload"), r.ws.close()
                }), this.ws.onclose = function (e) {
                    u("close event", e.code, e.reason), r.emit("close", e.code, e.reason), r._cleanup()
                }, this.ws.onerror = function (e) {
                    u("error event", e), r.emit("close", 1006, "WebSocket connection broken"), r._cleanup()
                }
            }

            r(c, a), c.prototype.send = function (e) {
                var t = "[" + e + "]";
                u("send", t), this.ws.send(t)
            }, c.prototype.close = function () {
                u("close");
                var e = this.ws;
                this._cleanup(), e && e.close()
            }, c.prototype._cleanup = function () {
                u("_cleanup");
                var e = this.ws;
                e && (e.onmessage = e.onclose = e.onerror = null), i.unloadDel(this.unloadRef), this.unloadRef = this.ws = null, this.removeAllListeners()
            }, c.enabled = function () {
                return u("enabled"), !!l
            }, c.transportName = "websocket", c.roundTrips = 2, t.exports = c
        }, {
            "../utils/event": 46,
            "../utils/url": 52,
            "./driver/websocket": 19,
            "debug": void 0,
            "events": 3,
            "inherits": 54
        }],
        39: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./lib/ajax-based"), i = e("./xdr-streaming"), s = e("./receiver/xhr"),
                a = e("./sender/xdr");

            function l(e) {
                if (!a.enabled) throw new Error("Transport created when disabled");
                o.call(this, e, "/xhr", s, a)
            }

            r(l, o), l.enabled = i.enabled, l.transportName = "xdr-polling", l.roundTrips = 2, t.exports = l
        }, {"./lib/ajax-based": 24, "./receiver/xhr": 32, "./sender/xdr": 34, "./xdr-streaming": 40, "inherits": 54}],
        40: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./lib/ajax-based"), i = e("./receiver/xhr"), s = e("./sender/xdr");

            function a(e) {
                if (!s.enabled) throw new Error("Transport created when disabled");
                o.call(this, e, "/xhr_streaming", i, s)
            }

            r(a, o), a.enabled = function (e) {
                return !e.cookie_needed && !e.nullOrigin && (s.enabled && e.sameScheme)
            }, a.transportName = "xdr-streaming", a.roundTrips = 2, t.exports = a
        }, {"./lib/ajax-based": 24, "./receiver/xhr": 32, "./sender/xdr": 34, "inherits": 54}],
        41: [function (e, t, n) {
            "use strict";
            var r = e("inherits"), o = e("./lib/ajax-based"), i = e("./receiver/xhr"), s = e("./sender/xhr-cors"),
                a = e("./sender/xhr-local");

            function l(e) {
                if (!a.enabled && !s.enabled) throw new Error("Transport created when disabled");
                o.call(this, e, "/xhr", i, s)
            }

            r(l, o), l.enabled = function (e) {
                return !e.nullOrigin && (!(!a.enabled || !e.sameOrigin) || s.enabled)
            }, l.transportName = "xhr-polling", l.roundTrips = 2, t.exports = l
        }, {
            "./lib/ajax-based": 24,
            "./receiver/xhr": 32,
            "./sender/xhr-cors": 35,
            "./sender/xhr-local": 37,
            "inherits": 54
        }],
        42: [function (l, u, e) {
            (function (e) {
                "use strict";
                var t = l("inherits"), n = l("./lib/ajax-based"), r = l("./receiver/xhr"), o = l("./sender/xhr-cors"),
                    i = l("./sender/xhr-local"), s = l("../utils/browser");

                function a(e) {
                    if (!i.enabled && !o.enabled) throw new Error("Transport created when disabled");
                    n.call(this, e, "/xhr_streaming", r, o)
                }

                t(a, n), a.enabled = function (e) {
                    return !e.nullOrigin && (!s.isOpera() && o.enabled)
                }, a.transportName = "xhr-streaming", a.roundTrips = 2, a.needBody = !!e.document, u.exports = a
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {
            "../utils/browser": 44,
            "./lib/ajax-based": 24,
            "./receiver/xhr": 32,
            "./sender/xhr-cors": 35,
            "./sender/xhr-local": 37,
            "inherits": 54
        }],
        43: [function (e, t, n) {
            (function (n) {
                "use strict";
                n.crypto && n.crypto.getRandomValues ? t.exports.randomBytes = function (e) {
                    var t = new Uint8Array(e);
                    return n.crypto.getRandomValues(t), t
                } : t.exports.randomBytes = function (e) {
                    for (var t = new Array(e), n = 0; n < e; n++) t[n] = Math.floor(256 * Math.random());
                    return t
                }
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        44: [function (e, t, n) {
            (function (e) {
                "use strict";
                t.exports = {
                    isOpera: function () {
                        return e.navigator && /opera/i.test(e.navigator.userAgent)
                    }, isKonqueror: function () {
                        return e.navigator && /konqueror/i.test(e.navigator.userAgent)
                    }, hasDomain: function () {
                        if (!e.document) return !0;
                        try {
                            return !!e.document.domain
                        } catch (e) {
                            return !1
                        }
                    }
                }
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        45: [function (e, t, n) {
            "use strict";
            var r, o = e("json3"),
                i = /[\x00-\x1f\ud800-\udfff\ufffe\uffff\u0300-\u0333\u033d-\u0346\u034a-\u034c\u0350-\u0352\u0357-\u0358\u035c-\u0362\u0374\u037e\u0387\u0591-\u05af\u05c4\u0610-\u0617\u0653-\u0654\u0657-\u065b\u065d-\u065e\u06df-\u06e2\u06eb-\u06ec\u0730\u0732-\u0733\u0735-\u0736\u073a\u073d\u073f-\u0741\u0743\u0745\u0747\u07eb-\u07f1\u0951\u0958-\u095f\u09dc-\u09dd\u09df\u0a33\u0a36\u0a59-\u0a5b\u0a5e\u0b5c-\u0b5d\u0e38-\u0e39\u0f43\u0f4d\u0f52\u0f57\u0f5c\u0f69\u0f72-\u0f76\u0f78\u0f80-\u0f83\u0f93\u0f9d\u0fa2\u0fa7\u0fac\u0fb9\u1939-\u193a\u1a17\u1b6b\u1cda-\u1cdb\u1dc0-\u1dcf\u1dfc\u1dfe\u1f71\u1f73\u1f75\u1f77\u1f79\u1f7b\u1f7d\u1fbb\u1fbe\u1fc9\u1fcb\u1fd3\u1fdb\u1fe3\u1feb\u1fee-\u1fef\u1ff9\u1ffb\u1ffd\u2000-\u2001\u20d0-\u20d1\u20d4-\u20d7\u20e7-\u20e9\u2126\u212a-\u212b\u2329-\u232a\u2adc\u302b-\u302c\uaab2-\uaab3\uf900-\ufa0d\ufa10\ufa12\ufa15-\ufa1e\ufa20\ufa22\ufa25-\ufa26\ufa2a-\ufa2d\ufa30-\ufa6d\ufa70-\ufad9\ufb1d\ufb1f\ufb2a-\ufb36\ufb38-\ufb3c\ufb3e\ufb40-\ufb41\ufb43-\ufb44\ufb46-\ufb4e\ufff0-\uffff]/g;
            t.exports = {
                quote: function (e) {
                    var t = o.stringify(e);
                    return i.lastIndex = 0, i.test(t) ? (r = r || function (e) {
                        var t, n = {}, r = [];
                        for (t = 0; t < 65536; t++) r.push(String.fromCharCode(t));
                        return e.lastIndex = 0, r.join("").replace(e, function (e) {
                            return n[e] = "\\u" + ("0000" + e.charCodeAt(0).toString(16)).slice(-4), ""
                        }), e.lastIndex = 0, n
                    }(i), t.replace(i, function (e) {
                        return r[e]
                    })) : t
                }
            }
        }, {"json3": 55}],
        46: [function (e, t, n) {
            (function (n) {
                "use strict";
                var r = e("./random"), o = {}, i = !1, s = n.chrome && n.chrome.app && n.chrome.app.runtime;
                t.exports = {
                    attachEvent: function (e, t) {
                        void 0 !== n.addEventListener ? n.addEventListener(e, t, !1) : n.document && n.attachEvent && (n.document.attachEvent("on" + e, t), n.attachEvent("on" + e, t))
                    }, detachEvent: function (e, t) {
                        void 0 !== n.addEventListener ? n.removeEventListener(e, t, !1) : n.document && n.detachEvent && (n.document.detachEvent("on" + e, t), n.detachEvent("on" + e, t))
                    }, unloadAdd: function (e) {
                        if (s) return null;
                        var t = r.string(8);
                        return o[t] = e, i && setTimeout(this.triggerUnloadCallbacks, 0), t
                    }, unloadDel: function (e) {
                        e in o && delete o[e]
                    }, triggerUnloadCallbacks: function () {
                        for (var e in o) o[e](), delete o[e]
                    }
                };
                s || t.exports.attachEvent("unload", function () {
                    i || (i = !0, t.exports.triggerUnloadCallbacks())
                })
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"./random": 50}],
        47: [function (t, p, e) {
            (function (f) {
                "use strict";
                var h = t("./event"), n = t("json3"), e = t("./browser"), d = function () {
                };
                p.exports = {
                    WPrefix: "_jp", currentWindowId: null, polluteGlobalNamespace: function () {
                        p.exports.WPrefix in f || (f[p.exports.WPrefix] = {})
                    }, postMessage: function (e, t) {
                        f.parent !== f ? f.parent.postMessage(n.stringify({
                            windowId: p.exports.currentWindowId,
                            type: e,
                            data: t || ""
                        }), "*") : d("Cannot postMessage, no parent window.", e, t)
                    }, createIframe: function (e, t) {
                        function n() {
                            d("unattach"), clearTimeout(i);
                            try {
                                a.onload = null
                            } catch (e) {
                            }
                            a.onerror = null
                        }

                        function r() {
                            d("cleanup"), a && (n(), setTimeout(function () {
                                a && a.parentNode.removeChild(a), a = null
                            }, 0), h.unloadDel(s))
                        }

                        function o(e) {
                            d("onerror", e), a && (r(), t(e))
                        }

                        var i, s, a = f.document.createElement("iframe");
                        return a.src = e, a.style.display = "none", a.style.position = "absolute", a.onerror = function () {
                            o("onerror")
                        }, a.onload = function () {
                            d("onload"), clearTimeout(i), i = setTimeout(function () {
                                o("onload timeout")
                            }, 2e3)
                        }, f.document.body.appendChild(a), i = setTimeout(function () {
                            o("timeout")
                        }, 15e3), s = h.unloadAdd(r), {
                            post: function (e, t) {
                                d("post", e, t), setTimeout(function () {
                                    try {
                                        a && a.contentWindow && a.contentWindow.postMessage(e, t)
                                    } catch (e) {
                                    }
                                }, 0)
                            }, cleanup: r, loaded: n
                        }
                    }, createHtmlfile: function (e, t) {
                        function n() {
                            clearTimeout(i), a.onerror = null
                        }

                        function r() {
                            u && (n(), h.unloadDel(s), a.parentNode.removeChild(a), a = u = null, CollectGarbage())
                        }

                        function o(e) {
                            d("onerror", e), u && (r(), t(e))
                        }

                        var i, s, a, l = ["Active"].concat("Object").join("X"), u = new f[l]("htmlfile");
                        u.open(), u.write('<html><script>document.domain="' + f.document.domain + '";<\/script></html>'), u.close(), u.parentWindow[p.exports.WPrefix] = f[p.exports.WPrefix];
                        var c = u.createElement("div");
                        return u.body.appendChild(c), a = u.createElement("iframe"), c.appendChild(a), a.src = e, a.onerror = function () {
                            o("onerror")
                        }, i = setTimeout(function () {
                            o("timeout")
                        }, 15e3), s = h.unloadAdd(r), {
                            post: function (e, t) {
                                try {
                                    setTimeout(function () {
                                        a && a.contentWindow && a.contentWindow.postMessage(e, t)
                                    }, 0)
                                } catch (e) {
                                }
                            }, cleanup: r, loaded: n
                        }
                    }
                }, p.exports.iframeEnabled = !1, f.document && (p.exports.iframeEnabled = ("function" == typeof f.postMessage || "object" == typeof f.postMessage) && !e.isKonqueror())
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"./browser": 44, "./event": 46, "debug": void 0, "json3": 55}],
        48: [function (e, t, n) {
            (function (n) {
                "use strict";
                var r = {};
                ["log", "debug", "warn"].forEach(function (e) {
                    var t;
                    try {
                        t = n.console && n.console[e] && n.console[e].apply
                    } catch (e) {
                    }
                    r[e] = t ? function () {
                        return n.console[e].apply(n.console, arguments)
                    } : "log" === e ? function () {
                    } : r.log
                }), t.exports = r
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        49: [function (e, t, n) {
            "use strict";
            t.exports = {
                isObject: function (e) {
                    var t = typeof e;
                    return "function" == t || "object" == t && !!e
                }, extend: function (e) {
                    if (!this.isObject(e)) return e;
                    for (var t, n, r = 1, o = arguments.length; r < o; r++) for (n in t = arguments[r]) Object.prototype.hasOwnProperty.call(t, n) && (e[n] = t[n]);
                    return e
                }
            }
        }, {}],
        50: [function (e, t, n) {
            "use strict";
            var i = e("crypto"), s = "abcdefghijklmnopqrstuvwxyz012345";
            t.exports = {
                string: function (e) {
                    for (var t = s.length, n = i.randomBytes(e), r = [], o = 0; o < e; o++) r.push(s.substr(n[o] % t, 1));
                    return r.join("")
                }, number: function (e) {
                    return Math.floor(Math.random() * e)
                }, numberString: function (e) {
                    var t = ("" + (e - 1)).length;
                    return (new Array(t + 1).join("0") + this.number(e)).slice(-t)
                }
            }
        }, {"crypto": 43}],
        51: [function (e, t, n) {
            "use strict";
            var o = function () {
            };
            t.exports = function (e) {
                return {
                    filterToEnabled: function (t, n) {
                        var r = {main: [], facade: []};
                        return t ? "string" == typeof t && (t = [t]) : t = [], e.forEach(function (e) {
                            e && ("websocket" !== e.transportName || !1 !== n.websocket ? t.length && -1 === t.indexOf(e.transportName) ? o("not in whitelist", e.transportName) : e.enabled(n) ? (o("enabled", e.transportName), r.main.push(e), e.facadeTransport && r.facade.push(e.facadeTransport)) : o("disabled", e.transportName) : o("disabled from server", "websocket"))
                        }), r
                    }
                }
            }
        }, {"debug": void 0}],
        52: [function (e, t, n) {
            "use strict";
            var r = e("url-parse"), o = function () {
            };
            t.exports = {
                getOrigin: function (e) {
                    if (!e) return null;
                    var t = new r(e);
                    if ("file:" === t.protocol) return null;
                    var n = t.port;
                    return n = n || ("https:" === t.protocol ? "443" : "80"), t.protocol + "//" + t.hostname + ":" + n
                }, isOriginEqual: function (e, t) {
                    var n = this.getOrigin(e) === this.getOrigin(t);
                    return o("same", e, t, n), n
                }, isSchemeEqual: function (e, t) {
                    return e.split(":")[0] === t.split(":")[0]
                }, addPath: function (e, t) {
                    var n = e.split("?");
                    return n[0] + t + (n[1] ? "?" + n[1] : "")
                }, addQuery: function (e, t) {
                    return e + (-1 === e.indexOf("?") ? "?" + t : "&" + t)
                }, isLoopbackAddr: function (e) {
                    return /^127\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$/i.test(e) || /^\[::1\]$/.test(e)
                }
            }
        }, {"debug": void 0, "url-parse": 58}],
        53: [function (e, t, n) {
            t.exports = "1.5.1"
        }, {}],
        54: [function (e, t, n) {
            "function" == typeof Object.create ? t.exports = function (e, t) {
                t && (e.super_ = t, e.prototype = Object.create(t.prototype, {
                    constructor: {
                        value: e,
                        enumerable: !1,
                        writable: !0,
                        configurable: !0
                    }
                }))
            } : t.exports = function (e, t) {
                if (t) {
                    e.super_ = t;

                    function n() {
                    }

                    n.prototype = t.prototype, e.prototype = new n, e.prototype.constructor = e
                }
            }
        }, {}],
        55: [function (e, a, l) {
            (function (s) {
                (function () {
                    var J = {"function": !0, "object": !0}, e = J[typeof l] && l && !l.nodeType && l,
                        B = J[typeof window] && window || this,
                        t = e && J[typeof a] && a && !a.nodeType && "object" == typeof s && s;

                    function F(e, l) {
                        e = e || B.Object(), l = l || B.Object();
                        var u = e.Number || B.Number, c = e.String || B.String, t = e.Object || B.Object,
                            v = e.Date || B.Date, n = e.SyntaxError || B.SyntaxError, b = e.TypeError || B.TypeError,
                            d = e.Math || B.Math, r = e.JSON || B.JSON;
                        "object" == typeof r && r && (l.stringify = r.stringify, l.parse = r.parse);
                        var y, o = t.prototype, g = o.toString, a = o.hasOwnProperty;

                        function w(e, t) {
                            try {
                                e()
                            } catch (e) {
                                t && t()
                            }
                        }

                        var p = new v(-0xc782b5b800cec);

                        function f(e) {
                            if (null != f[e]) return f[e];
                            var t;
                            if ("bug-string-char-index" == e) t = "a" != "a"[0]; else if ("json" == e) t = f("json-stringify") && f("date-serialization") && f("json-parse"); else if ("date-serialization" == e) {
                                if (t = f("json-stringify") && p) {
                                    var n = l.stringify;
                                    w(function () {
                                        t = '"-271821-04-20T00:00:00.000Z"' == n(new v(-864e13)) && '"+275760-09-13T00:00:00.000Z"' == n(new v(864e13)) && '"-000001-01-01T00:00:00.000Z"' == n(new v(-621987552e5)) && '"1969-12-31T23:59:59.999Z"' == n(new v(-1))
                                    })
                                }
                            } else {
                                var r, o = '{"a":[1,true,false,null,"\\u0000\\b\\n\\f\\r\\t"]}';
                                if ("json-stringify" == e) {
                                    var i = "function" == typeof (n = l.stringify);
                                    i && ((r = function () {
                                        return 1
                                    }).toJSON = r, w(function () {
                                        i = "0" === n(0) && "0" === n(new u) && '""' == n(new c) && n(g) === y && n(y) === y && n() === y && "1" === n(r) && "[1]" == n([r]) && "[null]" == n([y]) && "null" == n(null) && "[null,null,null]" == n([y, g, null]) && n({"a": [r, !0, !1, null, "\0\b\n\f\r\t"]}) == o && "1" === n(null, r) && "[\n 1,\n 2\n]" == n([1, 2], null, 1)
                                    }, function () {
                                        i = !1
                                    })), t = i
                                }
                                if ("json-parse" == e) {
                                    var s, a = l.parse;
                                    "function" == typeof a && w(function () {
                                        0 !== a("0") || a(!1) || (r = a(o), (s = 5 == r.a.length && 1 === r.a[0]) && (w(function () {
                                            s = !a('"\t"')
                                        }), s && w(function () {
                                            s = 1 !== a("01")
                                        }), s && w(function () {
                                            s = 1 !== a("1.")
                                        })))
                                    }, function () {
                                        s = !1
                                    }), t = s
                                }
                            }
                            return f[e] = !!t
                        }

                        if (w(function () {
                            p = -109252 == p.getUTCFullYear() && 0 === p.getUTCMonth() && 1 === p.getUTCDate() && 10 == p.getUTCHours() && 37 == p.getUTCMinutes() && 6 == p.getUTCSeconds() && 708 == p.getUTCMilliseconds()
                        }), f["bug-string-char-index"] = f["date-serialization"] = f.json = f["json-stringify"] = f["json-parse"] = null, !f("json")) {
                            var h = "[object Function]", x = "[object Number]", _ = "[object String]",
                                E = "[object Array]", m = f("bug-string-char-index"), j = function (e, t) {
                                    var n, s, r, o = 0;
                                    for (r in (n = function () {
                                        this.valueOf = 0
                                    }).prototype.valueOf = 0, s = new n) a.call(s, r) && o++;
                                    return n = s = null, (j = o ? function (e, t) {
                                        var n, r, o = g.call(e) == h;
                                        for (n in e) o && "prototype" == n || !a.call(e, n) || (r = "constructor" === n) || t(n);
                                        (r || a.call(e, n = "constructor")) && t(n)
                                    } : (s = ["valueOf", "toString", "toLocaleString", "propertyIsEnumerable", "isPrototypeOf", "hasOwnProperty", "constructor"], function (e, t) {
                                        var n, r, o = g.call(e) == h,
                                            i = !o && "function" != typeof e.constructor && J[typeof e.hasOwnProperty] && e.hasOwnProperty || a;
                                        for (n in e) o && "prototype" == n || !i.call(e, n) || t(n);
                                        for (r = s.length; n = s[--r];) i.call(e, n) && t(n)
                                    }))(e, t)
                                };
                            if (!f("json-stringify") && !f("date-serialization")) {
                                function S(e, t) {
                                    return ("000000" + (t || 0)).slice(-e)
                                }

                                var i = {92: "\\\\", 34: '\\"', 8: "\\b", 12: "\\f", 10: "\\n", 13: "\\r", 9: "\\t"},
                                    T = function (e) {
                                        var t, n, r, o, i, s, a, l, u;
                                        if (p) t = function (e) {
                                            n = e.getUTCFullYear(), r = e.getUTCMonth(), o = e.getUTCDate(), s = e.getUTCHours(), a = e.getUTCMinutes(), l = e.getUTCSeconds(), u = e.getUTCMilliseconds()
                                        }; else {
                                            function c(e, t) {
                                                return h[t] + 365 * (e - 1970) + f((e - 1969 + (t = +(1 < t))) / 4) - f((e - 1901 + t) / 100) + f((e - 1601 + t) / 400)
                                            }

                                            var f = d.floor,
                                                h = [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334];
                                            t = function (e) {
                                                for (o = f(e / 864e5), n = f(o / 365.2425) + 1970 - 1; c(n + 1, 0) <= o; n++) ;
                                                for (r = f((o - c(n, 0)) / 30.42); c(n, r + 1) <= o; r++) ;
                                                o = 1 + o - c(n, r), s = f((i = (e % 864e5 + 864e5) % 864e5) / 36e5) % 24, a = f(i / 6e4) % 60, l = f(i / 1e3) % 60, u = i % 1e3
                                            }
                                        }
                                        return (T = function (e) {
                                            return -1 / 0 < e && e < 1 / 0 ? (t(e), e = (n <= 0 || 1e4 <= n ? (n < 0 ? "-" : "+") + S(6, n < 0 ? -n : n) : S(4, n)) + "-" + S(2, r + 1) + "-" + S(2, o) + "T" + S(2, s) + ":" + S(2, a) + ":" + S(2, l) + "." + S(3, u) + "Z", n = r = o = s = a = l = u = null) : e = null, e
                                        })(e)
                                    };
                                if (f("json-stringify") && !f("date-serialization")) {
                                    function s(e) {
                                        return T(this)
                                    }

                                    var O = l.stringify;
                                    l.stringify = function (e, t, n) {
                                        var r = v.prototype.toJSON;
                                        v.prototype.toJSON = s;
                                        var o = O(e, t, n);
                                        return v.prototype.toJSON = r, o
                                    }
                                } else {
                                    function C(e) {
                                        var t = e.charCodeAt(0), n = i[t];
                                        return n || "\\u00" + S(2, t.toString(16))
                                    }

                                    function N(e) {
                                        return A.lastIndex = 0, '"' + (A.test(e) ? e.replace(A, C) : e) + '"'
                                    }

                                    var A = /[\x00-\x1f\x22\x5c]/g, k = function (e, t, n, r, o, i, s) {
                                        var a, l, u, c, f, h, d, p, m;
                                        if (w(function () {
                                            a = t[e]
                                        }), "object" == typeof a && a && (a.getUTCFullYear && "[object Date]" == g.call(a) && a.toJSON === v.prototype.toJSON ? a = T(a) : "function" == typeof a.toJSON && (a = a.toJSON(e))), n && (a = n.call(t, e, a)), a == y) return a === y ? a : "null";
                                        switch ("object" == (l = typeof a) && (u = g.call(a)), u || l) {
                                            case"boolean":
                                            case"[object Boolean]":
                                                return "" + a;
                                            case"number":
                                            case x:
                                                return -1 / 0 < a && a < 1 / 0 ? "" + a : "null";
                                            case"string":
                                            case _:
                                                return N("" + a)
                                        }
                                        if ("object" == typeof a) {
                                            for (d = s.length; d--;) if (s[d] === a) throw b();
                                            if (s.push(a), c = [], p = i, i += o, u == E) {
                                                for (h = 0, d = a.length; h < d; h++) f = k(h, a, n, r, o, i, s), c.push(f === y ? "null" : f);
                                                m = c.length ? o ? "[\n" + i + c.join(",\n" + i) + "\n" + p + "]" : "[" + c.join(",") + "]" : "[]"
                                            } else j(r || a, function (e) {
                                                var t = k(e, a, n, r, o, i, s);
                                                t !== y && c.push(N(e) + ":" + (o ? " " : "") + t)
                                            }), m = c.length ? o ? "{\n" + i + c.join(",\n" + i) + "\n" + p + "}" : "{" + c.join(",") + "}" : "{}";
                                            return s.pop(), m
                                        }
                                    };
                                    l.stringify = function (e, t, n) {
                                        var r, o, i, s;
                                        if (J[typeof t] && t) if ((s = g.call(t)) == h) o = t; else if (s == E) {
                                            i = {};
                                            for (var a, l = 0, u = t.length; l < u;) a = t[l++], "[object String]" != (s = g.call(a)) && "[object Number]" != s || (i[a] = 1)
                                        }
                                        if (n) if ((s = g.call(n)) == x) {
                                            if (0 < (n -= n % 1)) for (10 < n && (n = 10), r = ""; r.length < n;) r += " "
                                        } else s == _ && (r = n.length <= 10 ? n : n.slice(0, 10));
                                        return k("", ((a = {})[""] = e, a), o, i, r, "", [])
                                    }
                                }
                            }
                            if (!f("json-parse")) {
                                function I() {
                                    throw R = U = null, n()
                                }

                                function L() {
                                    for (var e, t, n, r, o, i = U, s = i.length; R < s;) switch (o = i.charCodeAt(R)) {
                                        case 9:
                                        case 10:
                                        case 13:
                                        case 32:
                                            R++;
                                            break;
                                        case 123:
                                        case 125:
                                        case 91:
                                        case 93:
                                        case 58:
                                        case 44:
                                            return e = m ? i.charAt(R) : i[R], R++, e;
                                        case 34:
                                            for (e = "@", R++; R < s;) if ((o = i.charCodeAt(R)) < 32) I(); else if (92 == o) switch (o = i.charCodeAt(++R)) {
                                                case 92:
                                                case 34:
                                                case 47:
                                                case 98:
                                                case 116:
                                                case 110:
                                                case 102:
                                                case 114:
                                                    e += q[o], R++;
                                                    break;
                                                case 117:
                                                    for (t = ++R, n = R + 4; R < n; R++) 48 <= (o = i.charCodeAt(R)) && o <= 57 || 97 <= o && o <= 102 || 65 <= o && o <= 70 || I();
                                                    e += M("0x" + i.slice(t, R));
                                                    break;
                                                default:
                                                    I()
                                            } else {
                                                if (34 == o) break;
                                                for (o = i.charCodeAt(R), t = R; 32 <= o && 92 != o && 34 != o;) o = i.charCodeAt(++R);
                                                e += i.slice(t, R)
                                            }
                                            if (34 == i.charCodeAt(R)) return R++, e;
                                            I();
                                        default:
                                            if (t = R, 45 == o && (r = !0, o = i.charCodeAt(++R)), 48 <= o && o <= 57) {
                                                for (48 == o && (48 <= (o = i.charCodeAt(R + 1)) && o <= 57) && I(), r = !1; R < s && (48 <= (o = i.charCodeAt(R)) && o <= 57); R++) ;
                                                if (46 == i.charCodeAt(R)) {
                                                    for (n = ++R; n < s && !((o = i.charCodeAt(n)) < 48 || 57 < o); n++) ;
                                                    n == R && I(), R = n
                                                }
                                                if (101 == (o = i.charCodeAt(R)) || 69 == o) {
                                                    for (43 != (o = i.charCodeAt(++R)) && 45 != o || R++, n = R; n < s && !((o = i.charCodeAt(n)) < 48 || 57 < o); n++) ;
                                                    n == R && I(), R = n
                                                }
                                                return +i.slice(t, R)
                                            }
                                            r && I();
                                            var a = i.slice(R, R + 4);
                                            if ("true" == a) return R += 4, !0;
                                            if ("fals" == a && 101 == i.charCodeAt(R + 4)) return R += 5, !1;
                                            if ("null" == a) return R += 4, null;
                                            I()
                                    }
                                    return "$"
                                }

                                function P(e, t, n) {
                                    var r = W(e, t, n);
                                    r === y ? delete e[t] : e[t] = r
                                }

                                var R, U, M = c.fromCharCode, q = {
                                    92: "\\",
                                    34: '"',
                                    47: "/",
                                    98: "\b",
                                    116: "\t",
                                    110: "\n",
                                    102: "\f",
                                    114: "\r"
                                }, D = function (e) {
                                    var t, n;
                                    if ("$" == e && I(), "string" == typeof e) {
                                        if ("@" == (m ? e.charAt(0) : e[0])) return e.slice(1);
                                        if ("[" == e) {
                                            for (t = []; "]" != (e = L());) n ? "," == e ? "]" == (e = L()) && I() : I() : n = !0, "," == e && I(), t.push(D(e));
                                            return t
                                        }
                                        if ("{" == e) {
                                            for (t = {}; "}" != (e = L());) n ? "," == e ? "}" == (e = L()) && I() : I() : n = !0, "," != e && "string" == typeof e && "@" == (m ? e.charAt(0) : e[0]) && ":" == L() || I(), t[e.slice(1)] = D(L());
                                            return t
                                        }
                                        I()
                                    }
                                    return e
                                }, W = function (e, t, n) {
                                    var r, o = e[t];
                                    if ("object" == typeof o && o) if (g.call(o) == E) for (r = o.length; r--;) P(g, j, o); else j(o, function (e) {
                                        P(o, e, n)
                                    });
                                    return n.call(e, t, o)
                                };
                                l.parse = function (e, t) {
                                    var n, r;
                                    return R = 0, U = "" + e, n = D(L()), "$" != L() && I(), R = U = null, t && g.call(t) == h ? W(((r = {})[""] = n, r), "", t) : n
                                }
                            }
                        }
                        return l.runInContext = F, l
                    }

                    if (!t || t.global !== t && t.window !== t && t.self !== t || (B = t), e) F(B, e); else {
                        var n = B.JSON, r = B.JSON3, o = !1, i = F(B, B.JSON3 = {
                            "noConflict": function () {
                                return o || (o = !0, B.JSON = n, B.JSON3 = r, n = r = null), i
                            }
                        });
                        B.JSON = {"parse": i.parse, "stringify": i.stringify}
                    }
                }).call(this)
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {}],
        56: [function (e, t, n) {
            "use strict";
            var i = Object.prototype.hasOwnProperty;

            function s(e) {
                try {
                    return decodeURIComponent(e.replace(/\+/g, " "))
                } catch (e) {
                    return null
                }
            }

            n.stringify = function (e, t) {
                t = t || "";
                var n, r, o = [];
                for (r in "string" != typeof t && (t = "?"), e) if (i.call(e, r)) {
                    if ((n = e[r]) || null != n && !isNaN(n) || (n = ""), r = encodeURIComponent(r), n = encodeURIComponent(n), null === r || null === n) continue;
                    o.push(r + "=" + n)
                }
                return o.length ? t + o.join("&") : ""
            }, n.parse = function (e) {
                for (var t, n = /([^=?&]+)=?([^&]*)/g, r = {}; t = n.exec(e);) {
                    var o = s(t[1]), i = s(t[2]);
                    null === o || null === i || o in r || (r[o] = i)
                }
                return r
            }
        }, {}],
        57: [function (e, t, n) {
            "use strict";
            t.exports = function (e, t) {
                if (t = t.split(":")[0], !(e = +e)) return !1;
                switch (t) {
                    case"http":
                    case"ws":
                        return 80 !== e;
                    case"https":
                    case"wss":
                        return 443 !== e;
                    case"ftp":
                        return 21 !== e;
                    case"gopher":
                        return 70 !== e;
                    case"file":
                        return !1
                }
                return 0 !== e
            }
        }, {}],
        58: [function (e, r, t) {
            (function (i) {
                "use strict";
                var d = e("requires-port"), p = e("querystringify"), s = /^[A-Za-z][A-Za-z0-9+-.]*:[\\/]+/,
                    n = /^([a-z][a-z0-9.+-]*:)?([\\/]{1,})?([\S\s]*)/i,
                    t = new RegExp("^[\\x09\\x0A\\x0B\\x0C\\x0D\\x20\\xA0\\u1680\\u180E\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200A\\u202F\\u205F\\u3000\\u2028\\u2029\\uFEFF]+");

                function m(e) {
                    return (e || "").toString().replace(t, "")
                }

                var v = [["#", "hash"], ["?", "query"], function (e) {
                        return e.replace("\\", "/")
                    }, ["/", "pathname"], ["@", "auth", 1], [NaN, "host", void 0, 1, 1], [/:(\d+)$/, "port", void 0, 1], [NaN, "hostname", void 0, 1, 1]],
                    a = {hash: 1, query: 1};

                function b(e) {
                    var t,
                        n = ("undefined" != typeof window ? window : void 0 !== i ? i : "undefined" != typeof self ? self : {}).location || {},
                        r = {}, o = typeof (e = e || n);
                    if ("blob:" === e.protocol) r = new g(unescape(e.pathname), {}); else if ("string" == o) for (t in r = new g(e, {}), a) delete r[t]; else if ("object" == o) {
                        for (t in e) t in a || (r[t] = e[t]);
                        void 0 === r.slashes && (r.slashes = s.test(e.href))
                    }
                    return r
                }

                function y(e) {
                    e = m(e);
                    var t = n.exec(e);
                    return {
                        protocol: t[1] ? t[1].toLowerCase() : "",
                        slashes: !!(t[2] && 2 <= t[2].length),
                        rest: t[2] && 1 === t[2].length ? "/" + t[3] : t[3]
                    }
                }

                function g(e, t, n) {
                    if (e = m(e), !(this instanceof g)) return new g(e, t, n);
                    var r, o, i, s, a, l, u = v.slice(), c = typeof t, f = this, h = 0;
                    for ("object" != c && "string" != c && (n = t, t = null), n && "function" != typeof n && (n = p.parse), t = b(t), r = !(o = y(e || "")).protocol && !o.slashes, f.slashes = o.slashes || r && t.slashes, f.protocol = o.protocol || t.protocol || "", e = o.rest, o.slashes || (u[3] = [/(.*)/, "pathname"]); h < u.length; h++) "function" != typeof (s = u[h]) ? (i = s[0], l = s[1], i != i ? f[l] = e : "string" == typeof i ? ~(a = e.indexOf(i)) && (e = "number" == typeof s[2] ? (f[l] = e.slice(0, a), e.slice(a + s[2])) : (f[l] = e.slice(a), e.slice(0, a))) : (a = i.exec(e)) && (f[l] = a[1], e = e.slice(0, a.index)), f[l] = f[l] || r && s[3] && t[l] || "", s[4] && (f[l] = f[l].toLowerCase())) : e = s(e);
                    n && (f.query = n(f.query)), r && t.slashes && "/" !== f.pathname.charAt(0) && ("" !== f.pathname || "" !== t.pathname) && (f.pathname = function (e, t) {
                        if ("" === e) return t;
                        for (var n = (t || "/").split("/").slice(0, -1).concat(e.split("/")), r = n.length, o = n[r - 1], i = !1, s = 0; r--;) "." === n[r] ? n.splice(r, 1) : ".." === n[r] ? (n.splice(r, 1), s++) : s && (0 === r && (i = !0), n.splice(r, 1), s--);
                        return i && n.unshift(""), "." !== o && ".." !== o || n.push(""), n.join("/")
                    }(f.pathname, t.pathname)), "/" !== f.pathname.charAt(0) && f.hostname && (f.pathname = "/" + f.pathname), d(f.port, f.protocol) || (f.host = f.hostname, f.port = ""), f.username = f.password = "", f.auth && (s = f.auth.split(":"), f.username = s[0] || "", f.password = s[1] || ""), f.origin = f.protocol && f.host && "file:" !== f.protocol ? f.protocol + "//" + f.host : "null", f.href = f.toString()
                }

                g.prototype = {
                    set: function (e, t, n) {
                        var r = this;
                        switch (e) {
                            case"query":
                                "string" == typeof t && t.length && (t = (n || p.parse)(t)), r[e] = t;
                                break;
                            case"port":
                                r[e] = t, d(t, r.protocol) ? t && (r.host = r.hostname + ":" + t) : (r.host = r.hostname, r[e] = "");
                                break;
                            case"hostname":
                                r[e] = t, r.port && (t += ":" + r.port), r.host = t;
                                break;
                            case"host":
                                r[e] = t, /:\d+$/.test(t) ? (t = t.split(":"), r.port = t.pop(), r.hostname = t.join(":")) : (r.hostname = t, r.port = "");
                                break;
                            case"protocol":
                                r.protocol = t.toLowerCase(), r.slashes = !n;
                                break;
                            case"pathname":
                            case"hash":
                                if (t) {
                                    var o = "pathname" === e ? "/" : "#";
                                    r[e] = t.charAt(0) !== o ? o + t : t
                                } else r[e] = t;
                                break;
                            default:
                                r[e] = t
                        }
                        for (var i = 0; i < v.length; i++) {
                            var s = v[i];
                            s[4] && (r[s[1]] = r[s[1]].toLowerCase())
                        }
                        return r.origin = r.protocol && r.host && "file:" !== r.protocol ? r.protocol + "//" + r.host : "null", r.href = r.toString(), r
                    }, toString: function (e) {
                        e && "function" == typeof e || (e = p.stringify);
                        var t, n = this, r = n.protocol;
                        r && ":" !== r.charAt(r.length - 1) && (r += ":");
                        var o = r + (n.slashes ? "//" : "");
                        return n.username && (o += n.username, n.password && (o += ":" + n.password), o += "@"), o += n.host + n.pathname, (t = "object" == typeof n.query ? e(n.query) : n.query) && (o += "?" !== t.charAt(0) ? "?" + t : t), n.hash && (o += n.hash), o
                    }
                }, g.extractProtocol = y, g.location = b, g.trimLeft = m, g.qs = p, r.exports = g
            }).call(this, "undefined" != typeof global ? global : "undefined" != typeof self ? self : "undefined" != typeof window ? window : {})
        }, {"querystringify": 56, "requires-port": 57}]
    }, {}, [1])(1)
});
//# sourceMappingURL=sockjs.min.js.map