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

    }
}
