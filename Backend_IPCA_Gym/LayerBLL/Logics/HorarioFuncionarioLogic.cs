using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;
using System.Data.SqlClient;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade HorarioFuncionario
    /// </summary>
    public class HorarioFuncionarioLogic
    {
        #region DEFAULT REQUESTS 

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os horarios
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            
            try
            {
                List<HorarioFuncionario> horarioFuncionarioList = await HorarioFuncionarioService.GetAllService(sqlDataSource);

                if (horarioFuncionarioList.Count != 0)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult(horarioFuncionarioList);
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidOperationException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de leitura dos dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (IndexOutOfRangeException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de acesso a uma coluna da base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um horario em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do HorarioFuncionario que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
           
            try
            {
                HorarioFuncionario horario = await HorarioFuncionarioService.GetByIDService(sqlDataSource, targetID);

                if (horario != null)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Horario obtido com sucesso!";
                    response.Data = new JsonResult(horario);
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidOperationException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de leitura dos dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (IndexOutOfRangeException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de acesso a uma coluna da base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um horario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newHorario">Objeto com os dados do HorarioFuncionario a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>

        public static async Task<Response> PostLogic(string sqlDataSource, HorarioFuncionario newHorario)
        {
            Response response = new Response();
            
            try
            {
                bool creationResult = await HorarioFuncionarioService.PostService(sqlDataSource, newHorario);

                if (creationResult)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult("Horario criado com sucesso!");
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um horario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="cliente">Objeto que contém os dados atualizados do horario</param>
        /// <param name="targetID">ID do HorarioFuncionario que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> PatchLogic(string sqlDataSource, HorarioFuncionario horario, int targetID)
        {
            Response response = new Response();
            
            try
            {
                bool updateResult = await HorarioFuncionarioService.PatchService(sqlDataSource, horario, targetID);

                if (updateResult)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult("Horario alterado com sucesso!");
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de apagar um horario
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do HorarioFuncionario que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            
            try
            {
                bool deleteResult = await HorarioFuncionarioService.DeleteService(sqlDataSource, targetID);

                if (deleteResult)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Success!";
                    response.Data = new JsonResult("Horario apagado com sucesso!");
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Leitura dos dados de todos os horários de um funcionário através do seu id de funcionário na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do funcionário que é pretendido retornar horarios</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<Response> GetAllByFuncionarioIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            
            try
            {
                List<HorarioFuncionario> horarioFuncionarioList = await HorarioFuncionarioService.GetAllByFuncionarioIDService(sqlDataSource, targetID);

                if (horarioFuncionarioList.Count != 0)
                {
                    response.StatusCode = StatusCodes.SUCCESS;
                    response.Message = "Horario obtido com sucesso!";
                    response.Data = new JsonResult(horarioFuncionarioList);
                }

                return response;
            }
            catch (SqlException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conexão com a base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidCastException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro na conversão de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (InvalidOperationException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de leitura dos dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (FormatException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de tipo de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (IndexOutOfRangeException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de acesso a uma coluna da base de dados: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (ArgumentNullException ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = "Erro de parametro inserido nulo: " + ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
            catch (Exception ex)
            {
                response.StatusCode = StatusCodes.NOCONTENT;
                response.Message = ex.Message;
                response.Data = new JsonResult(null);

                return response;
            }
        }

        #endregion
    }
}
