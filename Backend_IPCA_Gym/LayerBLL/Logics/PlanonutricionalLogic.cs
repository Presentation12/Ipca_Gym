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
    public class PlanoNutricionalLogic
    {
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
    }
}
