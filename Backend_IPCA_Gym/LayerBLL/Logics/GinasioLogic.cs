using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using System.Data.SqlClient;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Ginasio
    /// </summary>
    public class GinasioLogic
    {
        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os ginasios
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Ginasio> ginasioList = await GinasioService.GetAllService(sqlDataSource);

            if (ginasioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de ginásios obtida com sucesso!";
                response.Data = new JsonResult(ginasioList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um ginasio em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Ginasio que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Ginasio ginasio = await GinasioService.GetByIDService(sqlDataSource, targetID);

            if (ginasio != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Ginásio obtido com sucesso!";
                response.Data = new JsonResult(ginasio);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newGinasio">Objeto com os dados do Ginasio a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Ginasio newGinasio)
        {
            Response response = new Response();
            bool creationResult = await GinasioService.PostService(sqlDataSource, newGinasio);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Ginasio criado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="ginasio">Objeto que contém os dados atualizados do Ginasio</param>
        /// <param name="targetID">ID do Ginasio que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Ginasio ginasio, int targetID)
        {
            Response response = new Response();
            bool updateResult = await GinasioService.PatchService(sqlDataSource, ginasio, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Ginasio alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Ginasio que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await GinasioService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Ginasio apagado com sucesso!");
            }

            return response;
        }
    }
}
