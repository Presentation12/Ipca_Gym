﻿namespace Backend_IPCA_Gym.Models
{
    public class Refeicao
    {
        public int id_refeicao { get; set; }
        public int id_plano_nutricional { get; set; }
        public string descricao { get; set; }
        public TimeSpan hora { get; set; }
    }
}
