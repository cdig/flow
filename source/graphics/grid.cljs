;; Side Effects X
;; State X
;; Deps X

(ns graphics.grid)

(defn dot-position [dot-size step-size step-index offset]
  (-> step-index
      (* step-size)
      (+ (mod offset step-size))  ; using (mod) makes the grid seem "endless"
      (- step-size)               ; shift one row "back" so our extra rows are even
      (- (/ dot-size 2))))        ; draw the dot centered on this position

(defn generate [{:keys [x y z w h]}]
  (let [dot-size 5
        step-size 60
        wsteps (+ 2 (quot w step-size))  ; 2 extra rows for the edges of the screen
        hsteps (+ 2 (quot h step-size))]
    (for [hstep (range hsteps) wstep (range wsteps)]
      {:type :square
       :color "rgba(255,255,255,0.1)"
       :x (dot-position dot-size step-size wstep x)
       :y (dot-position dot-size step-size hstep y)
       :w dot-size})))
