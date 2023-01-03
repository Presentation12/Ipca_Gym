using System;
using System.Collections;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Services;
using System.Web.UI.WebControls;

namespace WebServiceIPCAGym.Services
{
    /// <summary>
    /// Serviço para efetuar o Login no IPCAGym
    /// </summary>
    [WebService(Namespace = "www.ipca.pt",
                Description = "Web service exemplo para a funcionalidade de Login [POST] do IPCAGym",
                Name = "IPCAGym")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class LoginWS : WebService
    {

        /// <summary>
        /// Class auxiliar para a receção de dados de login
        /// </summary>
        class LoginModel
        {
            public bool role;
            public int code;
            public string password;
            public string hash_pass;

            public LoginModel(bool role, int code, string password, string hash_pass)
            {
                this.role = role;
                this.code = code;
                this.password = password;
                this.hash_pass = hash_pass;
            }

            public LoginModel() { }
        }

        /// <summary>
        /// LoginWS de um funcionário
        /// </summary>
        /// <param name="codeInput">Codigo de autenticacao para efetuar login do funcionario</param>
        /// <param name="passwordInput">Password de autenticacao para efetuar login do funcionario</param>
        /// <returns>Role do funcionario em caso de sucesso</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidOperationException">Ocorre quando o codigo do funcionario nao está atribuido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        [WebMethod]
        public string Login(string codeInput, string passwordInput)
        {
            string query = @"
                            select * from dbo.Funcionario 
                            where codigo = @codigo and estado != 'Inativo'";

            string connectionString = "data source=DESKTOP-8AIBMTC;initial catalog=ipcagym;trusted_connection=true";
            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(connectionString))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("codigo", codeInput);
                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            string result = string.Empty;
                            LoginModel account = new LoginModel();

                            account.role = reader.GetBoolean(3);
                            account.code = reader.GetInt32(4);
                            account.password = reader.GetString(5);
                            account.hash_pass = reader.GetString(6);
                            string auxForAdmin = reader.GetString(2);

                            reader.Close();
                            databaseConnection.Close();

                            if (!VerifyPasswordHash(passwordInput, Convert.FromBase64String(account.hash_pass), Convert.FromBase64String(account.password)))
                            {
                                throw new ArgumentException("Password Errada.", "conta");
                            }
                            else
                            {
                                if (account.role == true)
                                {
                                    if (auxForAdmin == "adminaccount") result = "É admin!";
                                    else result = "É gerente!";
                                }
                                else
                                {
                                    result = "É funcionário";
                                }
                            }

                            Console.WriteLine(result);
                            return result;
                        }
                    }

                }
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Funcionário não existe\n" + ex.Message);
                return "Erro na autenticação";
            }
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return "Erro na autenticação";
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return "Erro na autenticação";
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return "Erro na autenticação";
            }
        }

        /// <summary>
        /// Método auxiliar para verificar se a password está correta
        /// </summary>
        /// <param name="password">Password inserida</param>
        /// <param name="passwordHash">Password encriptada</param>
        /// <param name="passwordSalt">Password normal</param>
        /// <returns>Resultado da operação em Boolean</returns>
        public static bool VerifyPasswordHash(string password, byte[] passwordHash, byte[] passwordSalt)
        {
            using (var hmac = new HMACSHA512(passwordSalt))
            {
                var computeHash = hmac.ComputeHash(Encoding.UTF8.GetBytes(password));
                return computeHash.SequenceEqual(passwordHash);
            }
        }
    }
}
