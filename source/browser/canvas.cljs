;; Wraps calls to the browser Canvas API
;; All functions should take and return the context, so they may be chained with ->

;; Side Effects √
;; State –
;; Deps √

(ns browser.canvas
  (:require [browser.document :as document]))

;; PROPERTY SETTERS

(defn fillStyle! [context value]
  (set! (.-fillStyle context) value)
  context)

(defn strokeStyle! [context value]
  (set! (.-strokeStyle context) value)
  context)

(defn lineWidth! [context value]
  (set! (.-lineWidth context) value)
  context)

(defn lineCap! [context value]
  (set! (.-lineCap context) value)
  context)

(defn lineJoin! [context value]
  (set! (.-lineJoin context) value)
  context)

;; FUNCTIONS

(defn clearRect! [context x y w h]
  (.clearRect context x y w h)
  context)

(defn fillRect! [context x y w h]
  (.fillRect context x y w h)
  context)

(defn rect! [context x y w h]
  (.rect context x y w h)
  context)

(defn moveTo! [context x y]
  (.moveTo context x y)
  context)

(defn lineTo! [context x y]
  (.lineTo context x y)
  context)

(defn arc! [context x y r start end ccw]
  (.arc context x y r start end ccw)
  context)

(defn beginPath! [context]
  (.beginPath context)
  context)

(defn closePath! [context]
  (.closePath context)
  context)

(defn fill! [context]
  (.fill context)
  context)

(defn stroke! [context]
  (.stroke context)
  context)

;; NICENESS

(defn canvas->context [canvas]
  (.getContext canvas "2d"))

(defn context->canvas [context]
  (.-canvas context))

(defn clear! [context]
  (let [canvas (context->canvas context)
        w (.-width canvas)
        h (.-height canvas)]
    (clearRect! context 0 0 w h)
    context))

(defn resize! [context w h]
  (let [canvas (context->canvas context)]
    (set! (.-width canvas) w)
    (set! (.-height canvas) h)
    context))

(defn create! []
  (-> (document/createElement! "canvas")
      document/appendChild!
      canvas->context))
