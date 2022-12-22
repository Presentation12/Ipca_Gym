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
    /// Classe que contém a lógica do response da entidade PlanoNutricional
    /// </summary>
    public class PlanoNutricionalLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os planos nutricionais
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PlanoNutricional> planonutricionalList = await PlanoNutricionalService.GetAllService(sqlDataSource);
            
            if (planonutricionalList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de planos nutricionais obtida com sucesso";
                response.Data = new JsonResult(planonutricionalList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um plano nutricional em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do PlanoNutricional que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            PlanoNutricional pnutricional = await PlanoNutricionalService.GetByIDService(sqlDataSource, targetID);

            if (pnutricional != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Plano Nutricional obtido com sucesso";
                response.Data = new JsonResult(pnutricional);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um plano nutricional
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newPlan">Objeto com os dados do PlanoNutricional a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, PlanoNutricional newPlan)
        {
            Response response = new Response();
            bool creationResult = await PlanoNutricionalService.PostService(sqlDataSource, newPlan);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Plano Nutricional criado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um plano nutricional
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="plan">Objeto que contém os dados atualizados do PlanoNutricional</param>
        /// <param name="targetID">ID do PlanoNutricional que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, PlanoNutricional plan, int targetID)
        {
            Response response = new Response();
            bool updateResult = await PlanoNutricionalService.PatchService(sqlDataSource, plan, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Plano Nutricioal alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um plano nutricional
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do PlanoNutricional que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await PlanoNutricionalService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Plano Nutricional apagado com sucesso!");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os planos de nutrição de um ginásio
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do ginásio que é pretendido retornar os planos de nutrição</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllByGinasioIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            List<PlanoNutricional> planoNutricionalList = await PlanoNutricionalService.GetAllByGinasioIDService(sqlDataSource, targetID);

            if (planoNutricionalList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de planos de treino obtida com sucesso";
                response.Data = new JsonResult(planoNutricionalList);
            }

            return response;
        }

        #endregion
    }
}
