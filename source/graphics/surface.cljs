;; Creates a renderable surface for drawing graphics.
;; To render, pass in a seq of renderable values.
;; This class exists to abstract the wrapped browser APIs.
;; The rest of the system can know the names of shapes — we know how to draw them.

;; Side Effects √
;; State X
;; Deps √

(ns graphics.surface
  (:require [app.db :as db]
            [browser.canvas :as canvas]
            [core.math :refer [TAU]]))

(defn- create!
  "Create a new surface object and cache it by key."
  [key]
  (let [surface {:key key
                 :context (canvas/create!)
                 :size nil}]
    (db/set-cache! key surface)
    surface))

(defn get-or-create!
  "Get the surface for the given key if it exists. Otherwise, create it."
  [key]
  (or (db/get-cache key) (create! key)))

(defn- set-size!
  "Make sure the given surface matches the given size"
  [{:keys [key size context]} w h]
  (when-not (= [w h] size)
    (canvas/resize! context w h)
    (db/update-cache! key assoc :size [w h])))

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
    (canvas/moveTo! context (+ x (+ sx 0.5)) (+ y (+ sy 0.5))))
  (doseq [[px py] (rest points)]
    (canvas/lineTo! context (+ x (+ px 0.5)) (+ y (+ py 0.5)))))

;; PUBLIC

(defn render!
  "Render the given elements to the surface named by key, resizing if necessary."
  [key w h elements]
  (let [surface (get-or-create! key)
        context (:context surface)]
    (set-size! surface w h)
    
    (canvas/clear! context)
    ; (canvas/lineCap! context "round")
    ; (canvas/lineJoin! context "round")
    (canvas/lineWidth! context "3")
    
    (doseq [{:keys [type stroke fill] :as entity} elements]
      (canvas/beginPath! context)
      
      (case type
        :square (render-square! context entity)
        :rect (render-rect! context entity)
        :circle (render-circle! context entity)
        :line (render-line! context entity)
        (prn "Surface: Unknown Type" type "for" entity "on surface" key))
      
      (canvas/closePath! context)

      (when fill
        (canvas/fillStyle! context fill)
        (canvas/fill! context))

      (when stroke
        (canvas/strokeStyle! context stroke)
        (canvas/stroke! context)))))
