;; The engine holds the state for the run loop for our app.
;; In the current implementation, it wraps the browser raf.

(ns app.engine
  (:require [browser.window :as window]))

(defonce last-time (atom 0))
(defonce callback (atom nil))
(defonce running (atom false))

(defn- tick
  "It's time to update the app. Calculate our dT since the last tick, and call the callback."
  [time-ms]
  (let [time (/ time-ms 1000)
        dT (- time @last-time)]
    (@callback dT)
    (reset! last-time time)
    (window/rAF! tick)))

(defn- first-tick!
  "The first tick after we start the engine is just a warmup — we don't call the callback. This keeps the first dT from being eratic."
  [time-ms]
  (reset! last-time (/ time-ms 1000))
  (window/rAF! tick))

;; PUBLIC

(defn start!
  "Start running the engine. Call the given callback on each tick. The callback should accept a single argument: the dT (delta time) in seconds since the last tick."
  [cb]
  (reset! callback cb)
  (when-not @running
    (reset! running true)
    (window/rAF! first-tick!)))
