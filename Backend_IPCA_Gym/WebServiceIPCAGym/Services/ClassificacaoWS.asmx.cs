using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Services;

namespace WebServiceIPCAGym.Services
{
    /// <summary>
    /// Serviço para efetuar a remoção de uma classificação no IPCAGym
    /// </summary>
    [WebService(Namespace = "www.ipca.pt",
                Description = "Web service exemplo para a funcionalidade de apagar uma classificação [DELETE] do IPCAGym",
                Name = "IPCAGym")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class ClassificacaoWS : WebService
    {
        /// <summary>
        /// Remoção de uma classificação da base de dados pelo seu ID
        /// </summary>
        /// <param name="ID_Classificação">ID da classificação a ser removida</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando um erro ou warning é retornado da base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        [WebMethod]
        public bool DeleteService(int ID_Classificação)
        {
            string query = @"
                            delete from dbo.Classificacao 
                            where id_avaliacao = @id_avaliacao";
            string connectionString = "data source=DESKTOP-8AIBMTC;initial catalog=ipcagym;trusted_connection=true";

            try
            {

                using (SqlConnection databaseConnection = new SqlConnection(connectionString))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_avaliacao", ID_Classificação);
                        int rowsAffected = myCommand.ExecuteNonQuery();

                        if (rowsAffected == 0) return false;

                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro/Warning no SQLServer: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Parâmetro inserido é nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
