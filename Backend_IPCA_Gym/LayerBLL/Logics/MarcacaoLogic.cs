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

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Marcacao
    /// </summary>
    public class MarcacaoLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as marcacoes
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Marcacao> marcacaoList = await MarcacaoService.GetAllService(sqlDataSource);

            if (marcacaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de marcacoes obtida com sucesso";
                response.Data = new JsonResult(marcacaoList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter uma marcacao em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Marcacao que é pretendida retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Marcacao marcacao = await MarcacaoService.GetByIDService(sqlDataSource, targetID);

            if (marcacao != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produto obtida com sucesso!";
                response.Data = new JsonResult(marcacao);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma marcacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newMarcacao">Objeto com os dados da Marcacao a ser criada</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Marcacao newMarcacao)
        {
            Response response = new Response();
            bool creationResult = await MarcacaoService.PostService(sqlDataSource, newMarcacao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao criada com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar uma marcacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="marcacao">Objeto que contém os dados atualizados da Marcacao</param>
        /// <param name="targetID">ID da Marcacao que é pretendida atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Marcacao marcacao, int targetID)
        {
            Response response = new Response();
            bool updateResult = await MarcacaoService.PatchService(sqlDataSource, marcacao, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao alterada com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar uma marcacao
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Marcacao que é pretendida eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await MarcacaoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao apagada com sucesso!");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as marcações de um funcionário através do seu id de funcionário na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Funcionário que é pretendida retornar as marcações</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllByFuncionarioIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            List<Marcacao> marcacaoList = await MarcacaoService.GetAllByFuncionarioIDService(sqlDataSource, targetID);

            if (marcacaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Marcações obtida com sucesso!";
                response.Data = new JsonResult(marcacaoList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma nova marcação na base de dados com o uso das regras de negócio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newMarcacao">Objeto com os dados da Marcacao a ser criada</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostCheckedLogic(string sqlDataSource, Marcacao newMarcacao)
        {
            Response response = new Response();
            bool creationResult = await MarcacaoService.PostCheckedService(sqlDataSource, newMarcacao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao criada com sucesso!");
            }

            return response;
        }

        #endregion
    }
}
