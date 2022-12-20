namespace LayerBOL.Models
{
    public class Atividade
    {
        public int id_atividade { get; set; }
        public int id_ginasio { get; set; }
        public int id_cliente { get; set; }
        public DateTime data_entrada { get; set; }
        public DateTime data_saida { get; set; }
    }
}
