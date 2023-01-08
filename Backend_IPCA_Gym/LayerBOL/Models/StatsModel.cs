using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LayerBOL.Models
{
    public class StatsModel
    {
        public int current { get; set; }
        public int exits { get; set; }
        public int today { get; set; }
        public double dailyAverage { get; set; }
        public double monthlyAverage { get; set; }
        public int yearTotal { get; set; }
        public int maxDay { get; set; }
        public int maxMonth { get; set; }
        public float averageMonday { get; set; }
        public float averageTuesday { get; set; }
        public float averageWednesday { get; set; }
        public float averageThursday { get; set; }
        public float averageFriday { get; set; }
        public float averageSaturday { get; set; }

    }
}
