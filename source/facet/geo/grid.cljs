;; Side Effects X
;; State X
;; Deps X

(ns facet.geo.grid
  (:require [gui.grid :as grid]))

(defn dot-position [thickness pitch step-index offset major-every]
  (-> step-index
      (* pitch)
      (+ (mod offset (* pitch major-every)))  ; using (mod) makes the grid seem "endless"
      (- (* pitch major-every))               ; shift some rows "back" so our overhang rows are even
      (- (/ thickness 2))                     ; draw the dot centered on this position
      (+ (* 0.5 (mod thickness 2)))))         ; align to the pixel grid

(defn major? [xstep ystep major-every]
  (or (zero? (mod xstep major-every))
      (zero? (mod ystep major-every))))

(defn get-scale [xstep ystep major-every major-size minor-size]
  (if (major? xstep ystep major-every)
      major-size
      minor-size))

(defn create
  [init]
  init)

(defn render
  [world {:keys [x y w h]}]
  (let [pitch (grid/get-pitch world)
        major-every (grid/get-major world)
        major-size 2
        minor-size 1
        overhang (* 2 major-every)
        xsteps (+ overhang (quot w pitch))  ; extra rows for the edges of the screen
        ysteps (+ overhang (quot h pitch))]
    (for [xstep (range xsteps) ystep (range ysteps)]
      (let [size (get-scale xstep ystep major-every major-size minor-size)]
          {:type :square
           :fill "#777"
           :x (dot-position size pitch xstep x major-every)
           :y (dot-position size pitch ystep y major-every)
           :w size}))))
