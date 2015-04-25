;; Creates a renderable surface for drawing graphics.
;; To render, pass in a seq of renderable values.
;; This class exists to abstract the wrapped browser APIs.
;; The rest of the system can know the names of shapes — we know how to draw them.

;; Side Effects √
;; State X
;; Deps √

(ns io.surface
  (:require [browser.canvas :as canvas]
            [core.math :refer [TAU]]))

(defonce contexts (atom {}))

(defn- create!
  "Create a new surface object and cache it by key."
  [key]
  (let [surface {:key key
                 :context (canvas/create!)
                 :size nil}]
    (swap! contexts assoc key surface)
    surface))

(defn get-or-create!
  "Get the surface for the given key if it exists. Otherwise, create it."
  [key]
  (or (get @contexts key) (create! key)))

(defn- set-size!
  "Make sure the given surface matches the given size"
  [{:keys [key size context]} w h]
  (when-not (= [w h] size)
    (canvas/resize! context w h)
    (swap! contexts assoc-in [key :size] [w h])))

(defn- render-square!
  [context {:keys [x y w]}]
  (canvas/rect! context x y w w))

(defn- render-rect!
  [context {:keys [x y w h]}]
  (canvas/rect! context x y w h))

(defn- render-circle!
  [context {:keys [x y r]}]
  (canvas/arc! context x y r 0 TAU false))

(defn- render-line!
  [context {:keys [x y points]}]
  (let [[[sx sy]] points]
    (canvas/moveTo! context (+ x sx) (+ y sy)))
  (doseq [[px py] (rest points)]
    (canvas/lineTo! context (+ x px) (+ y py))))

;; PUBLIC

(defn render!
  "Render the given elements to the surface named by key, resizing if necessary."
  [key w h elements]
  (let [surface (get-or-create! key)
        context (:context surface)]
    (set-size! surface w h)
    
    (canvas/clear! context)
    (canvas/lineCap! context "round")
    (canvas/lineJoin! context "round")
    (canvas/lineWidth! context "3")
    
    (doseq [{:keys [type stroke fill] :as object} elements]
      (canvas/beginPath! context)
      
      (case type
        :square (render-square! context object)
        :rect (render-rect! context object)
        :circle (render-circle! context object)
        :line (render-line! context object)
        (prn "Surface: Unknown Type" type "for" object "on surface" key))
      
      (canvas/closePath! context)

      (when fill
        (canvas/fillStyle! context fill)
        (canvas/fill! context))

      (when stroke
        (canvas/strokeStyle! context stroke)
        (canvas/stroke! context)))))
