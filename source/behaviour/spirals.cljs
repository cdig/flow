;; As good a test graphic as any

;; Side Effects √
;; State √
;; Deps √

(ns behaviour.spirals
  (:require [core.math :as math :refer [sinn sin cos scale denormalize random]]
            [app.messenger :as messenger]))

;; HELPERS

; (defn phasor [speed func min max]
;   (math/phasor (:phase @state) speed func min max))

;; STATE

; (def state (atom {:phase 0}))
;
; (messenger/register-receiver! :tick (fn [dT]
;   (reset! state (update @state :phase #(+ %1 dT)))))

;; FUNCTIONS

;; It'd be nice to use a component or behaviour system


; layers [{
;   :surface <surface reference>
;   :entities [{
;     :x
;     :y
;     :shapes {
;       :type <line circle rect square etc>
;       :color <solid, gradient, etc>
;       :x ; merged with element x/y
;       :y
;       :stroke-width ; extra shape-specific data if needed
;       :points [ ; if it's a line
;         :x
;         :y
;       ]
;     }
;   ]
;   }]





; (defn generate []
;   {:line {:width 20}
;    :points [
;             (let [range (phasor 1 sinn 200 200)
;                   wobble 0
;                   steps (phasor .5 sinn 2 20)
;                   spiral (phasor .5 sinn -100 -50)
;                   loops 2
;                   cx 250
;                   cy 250
;                   total-steps (* loops steps)]
;               (for [step total-steps]
;                 (let [i (scale step 0 total-steps 0 (* 360 loops))
;                       l (scale step 0 total-steps 0 loops)
;                       p (scale step 0 total-steps 0 1)
;                       decay (* l spiral)
;                       dist (+ (* wobble (denormalize (random) -1 1))
;                               decay
;                               range)
;                       px (+ cx (* (cos l) dist))
;                       py (+ cy (* (sin l) dist))]
;                   {:x px :y py})))
