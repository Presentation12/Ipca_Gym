using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL
{
    public class FuncionarioService
    { 
        public static async Task<List<Funcionario>> GetFuncionariosService(string sqlDataSource)
    {
        string query = @"select * from dbo.Funcionario";
        List<Funcionario> funcionarios = new List<Funcionario>();

        SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Funcionario funcionario = new Funcionario();

                        funcionario.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        funcionario.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        funcionario.nome = dataReader["nome"].ToString();
                        funcionario.is_admin = Convert.ToBoolean(dataReader["is_admin"]);
                        funcionario.codigo = Convert.ToInt32(dataReader["codigo"]);
                        funcionario.pass_salt = dataReader["pass_salt"].ToString();
                        funcionario.pass_hash = dataReader["pass_hash"].ToString();
                        funcionario.estado = dataReader["estado"].ToString();

                        funcionarios.Add(funcionario);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }
            return funcionarios;
        }
    }
}
