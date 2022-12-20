using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class HorarioFuncionarioService
    {
        public static async Task<List<HorarioFuncionario>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Horario_Funcionario";
            List<HorarioFuncionario> horarios = new List<HorarioFuncionario>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        HorarioFuncionario dia = new HorarioFuncionario();

                        dia.id_funcionario_horario = Convert.ToInt32(dataReader["id_funcionario_horario"]);
                        dia.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        dia.hora_entrada = (TimeSpan)dataReader["hora_entrada"];
                        dia.hora_saida = (TimeSpan)dataReader["hora_saida"];
                        dia.dia_semana = dataReader["dia_semana"].ToString();

                        horarios.Add(dia);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }
            return horarios;
        }
    }
}