;; One app.cljs to rule them all!

;; Side Effects √
;; State √
;; Deps √

(ns ^:figwheel-always app.app
  (:require [app.behaviour :as behaviour]
            [app.entity :as entity]
            [behaviour.all]
            [handlers.all]))

;; The big map of all entity descriptions with behaviours that describe the app
(def behaviours [
    
    ; {:bname :stinky
    ;  :state {:severity "slightly"}
    ;  :systems #{:stinker}}
    
    {:bname :follow-ball
     :state {
       :compose {
         :hi {}
         :test {}
       }
     }
     :systems #{:compose}}
  ])
  
(def entities [

    ; {:freyja [{:stinky {:severity "very"}}]}
    ;
    ; {:ivan [:stinky]}

    [:follow-ball]

  ])

; (def entities
;   (for [i (range 50)]
;     [:follow-ball]))

;; Create all entities and behaviours in the app map
(defn initialize! []

  (doseq [behaviour behaviours]
    (behaviour/register-template! behaviour))
  
  (doseq [e entities]
    (let [entity (if (map? e) e {nil e})
          name (first (keys entity))
          behaviours (first (vals entity))
          eid (entity/create! name)]
      (doseq [b behaviours]
        (let [behaviour (if (map? b) b {b {}})
              bname (first (keys behaviour))
              state (first (vals behaviour))]
          (behaviour/add! eid bname state))))))

(defonce initialized (do (initialize!) true))
