;; VIEWPORT
;; Currently, this mixes info about panning the viewport.. with the viewport entity (grid). Gross.

;; From the look of it, this is probably mostly Camera stuff

; gui.viewport

(ns hell.viewport
  (:require [facet.geo.grid :as grid]
            ; [facet.facet :as facet]
            [browser.window :as window]))

(def merge+ (partial merge-with +))
(def empty-pos {:x 0 :y 0})

; (defn- update-entity-size
;   [state]
;   (assoc state :grid merge {:w (window/width) :h (window/height)}))
;
; (defn- update-entity-pos
;   [state rel]
;   (update state :grid merge+ rel))

;; PUBLIC

(defn get-pos
  [world]
  (or (:viewport world)
      empty-pos))

(defn set-pos
  [world p]
  (assoc world :viewport p))

(defn move-pos
  [world rel]
  world)
  ; (-> world
  ;   ; (facet/change :gui ::viewport :grid update-entity-pos)
  ;   (update :viewport merge+ rel)))

(defn tick
  [world [event-type event-data]]
  world)
  ; (or
  ;   (case event-type
  ;
  ;     :mouse-drag
  ;     (when (= (:mode world) :navigating)
  ;       (move-pos world (:rel event-data)))
  ;
  ;     ; :resize
  ;     ; (facet/change world :gui ::viewport :grid update-entity-size)
  ;     )
  ;
  ;   world))

; (defn setup
;   [world]
;   (facet/attach world :gui ::viewport :grid {:x 0 :y 0 :w (window/width) :h (window/height)}))


; (defn get-size
;   []
;   )
;
; (defn renderable
;   [world]
;   (grid/render world (merge (get-pos world) (get-size))))
