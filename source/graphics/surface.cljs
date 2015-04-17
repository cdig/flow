;; Creates a renderable surface for drawing graphics.
;; To render, pass in a seq of renderable values.
;; This class exists to abstract the wrapped browser APIs.
;; The rest of the system can known the names of shapes — we know how to draw them.

;; Side Effects √
;; State X
;; Deps √

(ns graphics.surface
  (:require [browser.canvas :as canvas]
            [browser.window :as window]
            [core.math :refer [TAU]]))

(defn create! []
  (canvas/create! (window/width) (window/height)))

(defn render! [context elements]
    )
;   (canvas/clear! context)
;   (canvas/lineCap! context "round")
;   (canvas/lineJoin! context "round")
;
;   (doseq [{:keys [type color x y] :as data :or {:color "white"}} elements]
;     (canvas/beginPath! context)
;     (canvas/fillStyle! context color)
;     (canvas/strokeStyle! context color)
;
;     (case type
;       :square (canvas/rect! context x y (:w data) (:w data))
;       :rect (canvas/rect! context x y (:w data) (:h data))
;       :circle (canvas/arc! context x y (:r data) 0 TAU false))
;
;     (canvas/closePath! context)
;     (canvas/fill! context)
;     (canvas/stroke! context))
